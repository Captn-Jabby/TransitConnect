package com.connect.transitconnect.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "hops")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HopEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer cost;
    private Integer duration; // in minutes
    private String mode;

    @ManyToOne(cascade = CascadeType.PERSIST) // PERSIST --> if stop doesn't exist it is created
    @JoinColumn(name = "from_stop_id")
    private StopEntity fromStop;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "to_stop_id")
    private StopEntity toStop;

}
