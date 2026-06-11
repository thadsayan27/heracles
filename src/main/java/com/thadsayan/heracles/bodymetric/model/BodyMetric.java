package com.thadsayan.heracles.bodymetric.model;

import com.thadsayan.heracles.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "body_metric")
public class BodyMetric {

    @Id
    @Column(name = "user_id")
    private UUID userId;

    @OneToOne(optional = false)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private int weight;

    @Column(nullable = false)
    private int height;

}
