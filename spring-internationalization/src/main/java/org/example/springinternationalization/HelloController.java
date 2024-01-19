package org.example.springinternationalization;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

import static org.springframework.http.HttpHeaders.ACCEPT_LANGUAGE;

@RestController
@RequiredArgsConstructor
public class HelloController {

    private final MessageSource messageSource;

    @GetMapping("/hello")
    public String hello(HttpServletRequest httpServletRequest){
        return messageSource.getMessage("hello", null, httpServletRequest.getLocale());
    }

    @GetMapping("/dynamic-hello")
    public String dynamicHello(HttpServletRequest httpServletRequest, @RequestParam String name){
        return messageSource.getMessage("dynamic-hello", new Object[]{name}, httpServletRequest.getLocale());
    }

}
