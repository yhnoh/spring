package com.example.springlog;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LogController {

    @GetMapping("/logs")
    public void log(){
        log.trace("trace message");
        log.debug("debug message");
        log.info("info message");
        log.warn("warn message");
        log.error("error message");
    }

    @GetMapping("/logs/trace")
    public void trace(){
        log.trace("trace message");
    }

    @GetMapping("/logs/debug")
    public void debug(){
        log.debug("debug message");
    }

    @GetMapping("/logs/info")
    public void info(){
        log.info("info message");
    }

    @GetMapping("/logs/warn")
    public void warn(){
        log.warn("warn message");
    }

    @GetMapping("/logs/error")
    public void error(){
        log.error("error message");
    }

    @GetMapping("exception")
    public void exception(){ throw new RuntimeException("예외 발생");}
}
