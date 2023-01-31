package com.example.mapstruct.data_type_conversions._02_named_mapping_method_selection_based_on_qualifiers;

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
