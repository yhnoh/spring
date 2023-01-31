package com.example.mapstruct.data_type_conversions._02_named_mapping_method_selection_based_on_qualifiers;

import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 인자는 soruce, 결과값은 target
 */
@Named("NamedInvokedMapper")
public class NamedInvokedMapper {


    @Named("LocalDateTimeToString")
    public static String localDateTimeToString(LocalDateTime localDateTime) {
        return localDateTime != null ? localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null;
    }

    @Named("StringToLocalDateTime")
    public static LocalDateTime stringToLocalDateTime(String localDateTime) {
        return localDateTime != null ? LocalDateTime.parse(localDateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null;
    }

    @Named("StringToString")
    public static String stringToString(String string) {
        return string.replace("username", "username2");
    }
}
