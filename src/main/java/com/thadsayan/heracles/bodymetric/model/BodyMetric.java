package com.thadsayan.heracles.bodymetric.model;

import com.thadsayan.heracles.bodymetric.enums.Sex;
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Sex sex;

    @Column(nullable = false)
    private int weight;

    @Column(nullable = false)
    private int height;

}
