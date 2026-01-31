package com.connect.transitconnect.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteInputDTO {
    private List<StopDTO> stops; // length N
    private List<HopDTO> hops;   // length N-1: hop i is from stops[i] -> stops[i+1]
}
