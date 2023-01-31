package com.example.mapstruct.data_type_conversions._01_invoking_other_mapper;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class MemberDTO {
    private String username;
    private String createdDateTime;
    private LocalDateTime modifiedDateTime;

}
