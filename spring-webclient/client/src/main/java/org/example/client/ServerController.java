package org.example.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ServerController {


    @RequestMapping("/server/{id}")
    private ServerResponse<String> server(@PathVariable int id) throws InterruptedException {
        log.info("[서버-{}] 작업 시작", id);
        Thread.sleep(1000);
        ServerResponse<String> response = ServerResponse.success("서버 응답");
        log.info("[서버-{}] 작업 완료", id);
        return response;

    }
}
