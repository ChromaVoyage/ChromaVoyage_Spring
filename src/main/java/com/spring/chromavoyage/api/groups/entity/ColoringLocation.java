package com.spring.chromavoyage.api.groups.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@IdClass(ColoringLocationPk.class)
@Entity
@Data
@Table(name = "coloring_location")
public class ColoringLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "coloring_location_id")
    private Long coloringLocationId;

    @Id
    @Column(name = "group_id")
    private Long groupId;

    @Id
    @Column(name = "location_id")
    private Long locationId;

    @Column(name = "start_date")
    private Timestamp startDate;

    @Column(name = "end_date")
    private Timestamp endDate;


}
