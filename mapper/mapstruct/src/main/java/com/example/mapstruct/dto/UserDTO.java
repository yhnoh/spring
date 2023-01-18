package com.example.mapstruct.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

public class UserDTO {

    private Long id;

    private String username;
    private LocalDateTime registerDate;

}
