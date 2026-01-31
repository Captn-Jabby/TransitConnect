package com.connect.transitconnect.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HopDTO {
    private Integer cost;
    private Integer duration;
    private String mode;
}
