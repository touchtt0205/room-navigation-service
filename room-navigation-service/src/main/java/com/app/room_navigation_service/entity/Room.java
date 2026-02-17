//package com.app.room_navigation_service.entity;
//
//import jakarta.persistence.*;
//import lombok.*;
//
//import java.util.List;
//
//@Entity
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Table(name = "rooms")
//public class Room {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String name;
//    private String description;
//    private String imageUrl;
//    private Integer floor;
//
//    @ManyToOne
//    @JoinColumn(name = "building_id")
//    private Building building;
//
//    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
//    private List<Route> routes;
//
//}

package com.app.room_navigation_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "floor_id", nullable = false)
    private Floor floor;

    @Column(name = "has_shared_route", nullable = false)
    private Boolean hasSharedRoute = false;
}