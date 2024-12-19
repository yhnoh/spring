package org.example.client.response;

import org.example.client.ServerResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/server/response")
public class ServerResponseController {


    @GetMapping("/datas")
    public List<ServerResponse<String>> getDatas() {
        return IntStream.range(1, 11)
                .mapToObj(value -> ServerResponse.success("data" + value))
                .toList();
    }

    @GetMapping("/data")
    public ServerResponse<String> getData() {
        return ServerResponse.success("data");
    }
}
