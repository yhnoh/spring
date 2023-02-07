package com.example.springbatchstock;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.temporal.WeekFields;

public class DateTest {

    @Test
    public void test() {
        System.out.println();
        LocalDate.of(2022, 12, 31);
        System.out.println(LocalDate.now().get(WeekFields.ISO.weekOfYear()) + "");
    }
}
