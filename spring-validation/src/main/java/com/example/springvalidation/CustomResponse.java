package com.example.springvalidation;

import lombok.Getter;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.HashMap;
import java.util.Map;

@Getter
public class CustomResponse {
    private final int status;
    private final Map<String, String> errors = new HashMap<>();

    public CustomResponse(int status, MethodArgumentNotValidException e) {
        this.status = status;
        e.getBindingResult().getFieldErrors().stream().forEach(fieldError -> {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        });
    }

    public CustomResponse(int status, ConstraintViolationException e) {
        e.getConstraintViolations().stream()
                .forEach(cv -> errors.put(this.getPropertyName(cv.getPropertyPath()), cv.getMessage()));
        this.status = status;
    }

    private String getPropertyName(Path path) {
        String pathString = path.toString();
        return pathString.substring(pathString.lastIndexOf('.') + 1); // 전체 속성 경로에서 속성 이름만 가져온다.
    }
}
