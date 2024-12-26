package org.example.client.parallel;


import lombok.extern.slf4j.Slf4j;
import org.example.client.ServerResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/server/parallel")
public class ServerParallelController {

    @GetMapping("/{id}")
    public ServerResponse<String> getData(@PathVariable int id) throws InterruptedException {
        log.info("[서버 작업 시작] id={}", id);
        Thread.sleep(1000);
        ServerResponse<String> response = ServerResponse.success("data" + id);
        log.info("[서버 작업 종료] id={}", id);
        return response;
    }

    @GetMapping("/error/{id}")
    public ServerResponse<String> throwError(@PathVariable int id) throws InterruptedException {
        log.info("[서버 작업 시작] id={}", id);
        if (id % 2 == 0) {
            throw new RuntimeException("서버 작업 도중 에러 발생");
        }
        Thread.sleep(1000);
        ServerResponse<String> response = ServerResponse.success("data" + id);
        log.info("[서버 작업 종료] id={}", id);
        return response;
    }

}
