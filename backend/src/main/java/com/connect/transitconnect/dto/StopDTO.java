package com.connect.transitconnect.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StopDTO {
    private String location;
    private Double latitude;
    private Double longitude;
}
