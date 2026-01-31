package com.connect.transitconnect.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "stops")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StopEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String location;
    private Double latitude;
    private Double longitude;
}
