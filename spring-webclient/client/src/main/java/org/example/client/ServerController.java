package org.example.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ServerController {


    @RequestMapping("/v1/server/{id}")
    private ServerResponse<String> serverV1(@PathVariable int id) throws InterruptedException {
        log.info("[v1 서버-{}] 작업 시작", id);
        Thread.sleep(1000);
        ServerResponse<String> response = ServerResponse.success("data" + id);
        log.info("[v1 서버-{}] 작업 완료", id);
        return response;
    }

    @RequestMapping("/v2/server/{id}")
    private ServerResponse<List<String>> serverV2(@PathVariable int id) throws InterruptedException {
        log.info("[v2 서버-{}] 작업 시작", id);
        Thread.sleep(1000);
        ServerResponse<List<String>> response = ServerResponse.success(List.of("data" + id));
        log.info("[v2 서버-{}] 작업 완료", id);
        return response;
    }

    @RequestMapping("/v1/server/timeout")
    private ServerResponse<String> serverTimeout() throws InterruptedException {
        log.info("/v1/server/timeout 작업 시작");
        Thread.sleep(2000);
        ServerResponse<String> response = ServerResponse.success("data");
        log.info("/v1/server/timeout 작업 완료");
        return response;
    }

}
