package com.example.springvalidation;

import lombok.Getter;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.ArrayList;
import java.util.List;

@Getter
public class CustomResponse {
    private final int status;
    private final List<ResponseError> errors = new ArrayList<>();

    public CustomResponse(int status, MethodArgumentNotValidException methodArgumentNotValidException) {
        this.status = status;
        methodArgumentNotValidException.getBindingResult().getFieldErrors().stream().forEach(fieldError -> {
            ResponseError responseError = new ResponseError(fieldError.getField(), fieldError.getDefaultMessage());
            errors.add(responseError);
        });
    }

    public CustomResponse(int status, ConstraintViolationException constraintViolationException) {
        constraintViolationException.getConstraintViolations().stream()
                .forEach(constraintViolation -> {
                    ResponseError responseError = new ResponseError(this.getPropertyName(constraintViolation.getPropertyPath()), constraintViolation.getMessage());
                    errors.add(responseError);
                });
        this.status = status;
    }

    private String getPropertyName(Path path) {
        String pathString = path.toString();
        return pathString.substring(pathString.lastIndexOf('.') + 1); // 전체 속성 경로에서 속성 이름만 가져온다.
    }

    @Getter
    static class ResponseError {
        private final String property;
        private final String message;

        public ResponseError(String property, String message) {
            this.property = property;
            this.message = message;
        }
    }
}
