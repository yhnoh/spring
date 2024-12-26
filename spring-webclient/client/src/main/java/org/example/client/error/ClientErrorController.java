package org.example.client.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@RestController
@RequestMapping("/client/error")
@Slf4j
public class ClientErrorController {

    private WebClient webClient() {

        // 1초로 타임아웃 설정
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofMillis(1000));
        ReactorClientHttpConnector connector = new ReactorClientHttpConnector(httpClient);

        return WebClient.builder()
                .clientConnector(connector)
//                .defaultStatusHandler(httpStatusCode -> {
//                    log.info("어떤 에러를 핸들링 할지 확인 : {}", httpStatusCode);
//                    return httpStatusCode.is4xxClientError() || httpStatusCode.is5xxServerError();
//                }, clientResponse -> clientResponse.bodyToMono(ClientError.class)
//                        .flatMap(clientError -> {
//                            log.info("에러 발생 이후 RuntimeException 발생 시키기 : {}", clientError);
//                            return Mono.error(new RuntimeException(clientError.getMessage()));
//                        }))
                .baseUrl("http://localhost:8080/server/error")
                .defaultHeaders(httpHeaders -> httpHeaders.setContentType(MediaType.APPLICATION_JSON))
                .build();
    }


    /**
     * 에러 핸들링을 하지 않을 경우 WebClientResponseException 발생
     */
    @GetMapping("/400")
    public void error400() {
        webClient().get()
                .uri("/400")
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    /**
     * 에러 핸들링을 하지 않을 경우 WebClientResponseException 발생
     */
    @GetMapping("/500")
    public void error500() {
        webClient().get()
                .uri("/500")
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    /**
     * 에러 핸들링을 하지 않을 경우 WebClientResponseException 발생
     */
    @GetMapping("/timeout")
    public void errorTimeout() {
        webClient()
                .get()
                .uri("/timeout")
                .retrieve()
                .bodyToMono(Void.class)
                .timeout(Duration.ofMillis(1000))
                .block();
    }

    /**
     * onStatus 에러 핸들링
     * 에러 발생시 status 코드를 이용하여 다른 에러로 반환
     */
    @GetMapping("/onStatus")
    public void onStatus() {
        try {
            webClient().get()
                    .uri("/400")
                    .retrieve()
                    .onStatus(httpStatusCode -> {
                        boolean is4xxClientError = httpStatusCode.is4xxClientError();
                        log.info("400 에러 핸들링 여부 확인 status: {}, is4xxClientError: {}", httpStatusCode, is4xxClientError);
                        return is4xxClientError;
                    }, clientResponse -> Mono.error(new RuntimeException("400 에러 발생")))
                    .bodyToMono(Void.class)
                    .block();
        } catch (RuntimeException e) {
            log.info("400 에러 발생시 RuntimeException 발생: {}", e.getMessage());
        }
    }

    /**
     * 에러 발생시 해당 에러를 catch하여 다른 결과값으로 반환
     */
    @GetMapping("/onErrorResume")
    public void onErrorResume() {

        webClient().get()
                .uri("/400")
                .retrieve()
                .bodyToMono(Void.class)
                .onErrorResume(WebClientResponseException.class, e -> {
                    log.info("onErrorResume -> {} 에러 발생으로 Mono.empty()로 반환", ClassUtils.getShortName(e.getClass()));
                    return Mono.empty();
                })
                .block();

    }

    /**
     * 에러 발생시 다른 에러로 반환
     */
    @GetMapping("/onErrorMap")
    public void onErrorMap() {

        try {
            webClient().get()
                    .uri("/400")
                    .retrieve()
                    .bodyToMono(Void.class)
                    .onErrorMap(WebClientResponseException.class, e -> {
                        log.info("onErrorMap  -> {} 에러 발생으로 RuntimeException 으로 반환", ClassUtils.getShortName(e.getClass()));
                        return new RuntimeException(ClassUtils.getShortName(e.getClass()) + " -> RuntimeException");
                    })
                    .block();
        } catch (RuntimeException e) {
            log.info("RuntimeException 발생: {}", e.getMessage());
        }

    }

    @GetMapping("/onErrorMap2")
    public void onErrorMap2() {

        try {
            webClient().get()
                    .uri("/timeout")
                    .retrieve()
                    .bodyToMono(Void.class)
//                    .timeout(Duration.ofMillis(3000))
                    .onErrorMap(WebClientRequestException.class, e -> {
                        log.info("onErrorMap  -> {} 에러 발생으로 RuntimeException 으로 반환", ClassUtils.getShortName(e.getCause().getClass()));
                        return new RuntimeException(ClassUtils.getShortName(e.getClass()) + " -> RuntimeException");
                    })
                    .onErrorMap(WebClientResponseException.class, e -> {
                        log.info("onErrorMap  -> {} 에러 발생으로 RuntimeException 으로 반환", ClassUtils.getShortName(e.getCause().getClass()));
                        return new RuntimeException(ClassUtils.getShortName(e.getClass()) + " -> RuntimeException");
                    })
                    .block();
        } catch (RuntimeException e) {
            log.info("RuntimeException 발생: {}", e.getMessage());
        }

    }
//
//    /**
//     * 400 에러
//     */
//    @GetMapping("/500")
//    public ClientError error500() {
//        return webClient().get()
//                .uri("/500")
//                .retrieve()
//                .bodyToMono(ClientError.class)
//                .block();
//    }

}
