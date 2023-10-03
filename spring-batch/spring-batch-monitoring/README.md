
### Spring Batch 모니터링

### 메트릭 수집

### PushGateway + Prometheus + 
- 프로메테우스는 기본적으로 메트릭 데이터를 수집하기 위해서 HTTP 통신을 통해서 pull한다.
    > time series collection happens via a pull model over HTTP
- 스프링 배치의 경우에는 HTTP통신을 통해서 잡을 실행하는 경우 말고도, cli를 통해서 잡을 실행하고 종료하여 프로그램 자체가 종료되는 경우가 있다
  - cli를 통해서 잡을 실행하고 종료하는 상황에서는 프로메테우스가 메트릭 데이터를 수집할 수 없다.
- 때문에 메트릭 데이터를 중간에 저장하고 있는 중개 게이트웨이가 필요하며, 이 게이트웨를 통해서 프로 프로메테우스에게 push 할 수 있는 중간 게이트웨이가 필요하다.
- Pushgateway는 메트릭을 Push할 수 있도록 지원하며 Push된 매트릭을 Prometheus가 Pulling하여 가져갈 수 있도록 중개자 역할을 한다.
- 

> https://prometheus.io/docs/introduction/overview/