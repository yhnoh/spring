package com.example.springcloudstreamgoods;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Builder
@Getter
public class GoodsRegistrationRequest {

    @NotBlank(message = "상품 이름을 입력해 주세요.")
    private String name;
    @Min(value = 0, message = "상품 수량은 0이하일 수 없습니다.")
    private int quantity;
    @Min(value = 0, message = "상품 가격은 0이하일 수 없습니다.")
    private int amount;


}
