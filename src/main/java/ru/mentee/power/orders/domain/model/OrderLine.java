package ru.mentee.power.orders.domain.model;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderLine {
    private UUID productId;
    private Integer quantity;
    private Double price;
    private String priority;
}
