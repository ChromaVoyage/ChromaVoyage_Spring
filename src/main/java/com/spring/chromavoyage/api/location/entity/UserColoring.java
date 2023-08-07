package com.spring.chromavoyage.api.location.entity;
import lombok.Data;
import javax.persistence.*;

@IdClass(UserColoringPk.class)
@Entity
@Data
@Table(name = "user_coloring")
public class UserColoring {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_coloring_id")
    private Long usercoloringId;

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Id
    @Column(name = "location_id")
    private Long locationId;
}