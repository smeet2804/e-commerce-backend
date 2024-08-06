package com.example.cartservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RemoveFromCartRequestDTO {
    private Long userId;
    private Long productId;
}
