package com.example.mapstruct.data_type_conversions._01_invoking_other_mapper;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Builder
@Getter
public class Member {
    @Builder.Default
    private String username = "username";
    @Builder.Default
    private LocalDateTime createdDateTime = LocalDateTime.of(2023, 01, 31, 0, 0);
    @Builder.Default
    private String modifiedDateTime = LocalDateTime.of(2023, 01, 31, 0, 0).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);


}
