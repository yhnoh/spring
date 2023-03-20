### Spring Batch 무중단 배포
---

### 배포시 어떠한 상활에 처하는가?
- 현재 내가 제작 중인 프로젝트에서 Spring Batch는 젠킨스에 있는 스케쥴러 기능 통해서 원격 서버에 있는 jar파일을 실행시켜 Job이 실행시킨다.
- 스케쥴러가 1분 단위로도 Batch를 실행시킬 수 있기 때문에, 거의 24시간 멈추지않고 실행하고 있다 해도 무방하다.
- 이 상황에서 배포를 하게 될 경우 아래와 같은 고민을 할 수 있다?
  1. 실행중인 Job을 중간에 종료시키고 배포를 할 것인가?
      - 중간에 종료한다는 의미는 다시 Job을 재실행시켜줘야 한다는 의미가 된다.
      - 이렇게 될 경우 어떤 Job이 중지되었는지를 전부 체크해야하며, 어떻게 재실행을 할지에 대한 고민을 해봐야한다.
  2. 아니면 Job이 모두 종료되도록 기다리고 난 다음에 배포를 할 것인가?
      - 만약 계속해서 Job이 종료되지 않고 실행되고 있다면 어떻게 배포를 진행할 것인가?
      - 기다릴 것인가? 언제까지? 만약 계속해서 실행되고 있다면?

#### 그냥 배포하면되지 뭐가 문제인데?
- 위 상황에서 그러한 생각을 할 수 있다. 왜 그냥 배포하면 안되냐?
  - 이미 실행중인 Job은 기존 jar파일에서 실행될것이고 새롭게 실행된 Job은 새로운 jar파일에서 실행되는 것 아닌가?
  - 만약 젠킨스 파이프라인으로 각각의 Spring Batch Job을 실행시키고 있는 상황이라면 어떻겠는가?
    - 파이프라인에서 Job1과 Job2를 실행하는 상황
      - 가정) Job1 다음에 Job2를 실행, Job2는 Job1에 종속적
      - Job1 실행중 -> 배포 -> Job1 실행 완료 -> Job2 실행 (새롭게 배포된 jar파일을 가지고 배치 실행)
      - 여기서 갑자기 Job2는 새롭게 배포된 jar파일을 가지고 Job1에 종속적인 상황이여서 Job이 실패할 수 있다.
      - ***즉 기존에 실행시켰던 파이프라인이면 기존 jar파일을 그대로 실행시켜야하고, 새롭게 실행되는 파이프라인은 새롭게 배포된 jar파일을 실행시켜야한다. ***
  - 다른 문제점은 과연 jar파일 하나만 배포가 될 것인가? 배포시 여러 파일들을 배포하게 되는 경우가 있을 수 있다.
  - 또한 배포 진행 중의 상황 (jar파일이 삭제되었다가 생성되는 찰나의 순간)과 Job을 실행시키는 상황이 갑자기 겹쳐서 실행이 안되는 경우도 발생할 수 있다.
> ***배포시 프로그램 오류를 야기시킬 수 있는 일이 발생할 수 있다.***

#### 스프링 배치 무중단 배포 해보기
1. 배포를 매번 새로운 경로에 한다.
    ```shell
    #!/bin/sh
    # 디렉토리 설정
    SSH_USER_HOME_DIRECTORY_NAME=$(ssh $UBUNTU_NAME@$DEPLOY_IP "pwd");
    # 임시 디렉토리 생성
    DIRECTORY_NAME=deploy/batch_$(/bin/date +%Y%m%d%H%M%S);
    # 원격 서버에 임시 디렉토리 생성
    ssh $UBUNTU_NAME@$DEPLOY_IP "mkdir -p $SSH_USER_HOME_DIRECTORY_NAME/$DIRECTORY_NAME";
    # jar파일을 임시 디렉토리에 전달
    scp build/libs/*.jar $UBUNTU_NAME@$DEPLOY_IP:$SSH_USER_HOME_DIRECTORY_NAME/$DIRECTORY_NAME;
    ```
    - build이후 jar파일이 만들어지면 원격 서버에 임시 디렉토리를 생성하여 jar파일을 전달한다.
- 심볼릭 링크를 이용해 새롭게 배포된 경로로 즉시 변경한다.
    ```shell
    LINK_NAME=deploy/batch;
    ssh $UBUNTU_NAME@$DEPLOY_IP "ln -Tfs $SSH_USER_HOME_DIRECTORY_NAME/$DIRECTORY_NAME $SSH_USER_HOME_DIRECTORY_NAME/$LINK_NAME";
    ```
    - `ln -Tfs` 명령어를 통해서 deploy/batch 경로에 임시 디렉토리를 연결한다.
      - `ln -Tfs` 명령어 소프트링크 관련 내용을 찾아보면 좋다. 
- 심볼릭 링크가 가리키는 원래 링크에서 Job을 실행한다.
    
    ```shell
    JAR_PATH=$(ssh $UBUNTU_NAME@$DEPLOY_IP "readlink -f $SSH_USER_HOME_DIRECTORY_NAME/$LINK_NAME/ci-cd-jenkins-code-0.0.1-SNAPSHOT.jar");
    ```
    - `readlink` 명령어를 통해서 `deploy/batch`의 실제 링크를 가져와서 jar 파일을 실행시킨다.
    - 위와 같은 방식으로 기존에 실행중인 Job은 기존 모듈을 바라보며 실행이 되고, 새롭게 배포된 jar파일이 기존 Job에 영향을 끼치지 않는다.
    - 새롭게 실행되는 Job은 `readlink` 명령어를 통해서 새롭게 배포된 경로를 읽어오기 때문에 기존에 배포된 jar를 실행시키지 않는다.
- 최신 배포된 디렉토리만 유지시킨다.
    ```shell
    ### 최신 3개를 제외한 나머지 디렉토리를 삭제한다.
    ssh $UBUNTU_NAME@$DEPLOY_IP "ls deploy -t|tail -n +4|xargs rm -rf"
    ```

### 후기
---

jar 파일이 실행된 상황에서 jar파일이 삭제가 되어도 실제로 에러가 나지는 않지만, 단일 jar파일만 배포하지 않기 때문에 임시 디렉토리를 생성하여 심볼릭 링크로 연결하여, 새롭게 실행되는 Job은 새롭게 배포된 jar파일을 실행시키도록 예시를 작성해 보았다. 현재 처음 Spring Batch 프로젝트와 배포 및 시스템 운영 관련하여 여러 자료를 찾아보던 도중 도움이 될만한 자료를 찾아 매우 다행이다 싶다. 리눅스 및 shell sciprt 명령어가 미숙하여 생각보다 오래 걸렸으니 해당 내용에 대해 공부좀 많이 해야겠다. 역시 그냥 아는 것과 활용하는 것은 전혀 다른 문제라는것을 이번에 깨닫는다.

> https://taetaetae.github.io/2019/10/13/batch-nondisruptive-deploy/ <br/>
> https://www.jigi.net/os/6006/ <br>