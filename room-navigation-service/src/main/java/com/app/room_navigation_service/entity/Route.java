package com.app.room_navigation_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Route")
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String startFrom;
    @Column(name = "room_id")
    private Long roomId;

//    @ManyToOne
//    @JoinColumn(name = "room_id")
//    private Room room;

//    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL)
//    private List<RouteStep> routeSteps;
}
