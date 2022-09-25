package com.example.springscheduler;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeTest {

    @Test
    public void strLocalDateTime() {

        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        System.out.println("now = " + now);
        //give

        //when

        //then
    }

    @Test
    public void test(){
        System.out.println(Runtime.getRuntime().availableProcessors());
    }
}
