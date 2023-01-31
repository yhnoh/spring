package com.example.mapstruct.data_type_conversions._01_invoking_other_mapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 인자는 soruce, 결과값은 target
 */
public class InvokedMapper {


    public static String localDateTimeToString(LocalDateTime localDateTime) {
        return localDateTime != null ? localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null;
    }

    public static LocalDateTime stringToLocalDateTime(String localDateTime) {
        return localDateTime != null ? LocalDateTime.parse(localDateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null;
    }

    public static String stringToString(String string) {
        return string.replace("username", "username2");
    }
}
