package com.example.mapstruct.data_type_conversions._03_annotated_mapping_method_selection_based_on_qualifiers;

import com.example.mapstruct.data_type_conversions._03_annotated_mapping_method_selection_based_on_qualifiers.annotation.LocalDateTimeToString;
import com.example.mapstruct.data_type_conversions._03_annotated_mapping_method_selection_based_on_qualifiers.annotation.StringToLocalDateTime;
import com.example.mapstruct.data_type_conversions._03_annotated_mapping_method_selection_based_on_qualifiers.annotation.StringToString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 인자는 soruce, 결과값은 target
 */
public class AnnotatedInvokedMapper {

    @LocalDateTimeToString
    public static String localDateTimeToString(LocalDateTime localDateTime) {
        return localDateTime != null ? localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null;
    }

    @StringToLocalDateTime
    public static LocalDateTime stringToLocalDateTime(String localDateTime) {
        return localDateTime != null ? LocalDateTime.parse(localDateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null;
    }

    @StringToString
    public static String stringToString(String string) {
        return string.replace("username", "username2");
    }
}
