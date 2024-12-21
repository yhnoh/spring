package org.example.client.error;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/server/error")
public class ServerErrorController {

    @GetMapping("/400")
    public ResponseEntity error400() throws InterruptedException {
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/500")
    public ResponseEntity error500() throws InterruptedException {
        return ResponseEntity.internalServerError().build();
    }

    @GetMapping("/timeout")
    public void timeout() throws InterruptedException {
        Thread.sleep(2000);
    }

}
