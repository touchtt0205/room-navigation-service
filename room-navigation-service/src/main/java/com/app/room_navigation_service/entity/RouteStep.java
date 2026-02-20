package com.app.room_navigation_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "route_steps")
public class RouteStep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "route_id")
    private Long routeId;

    @Column(name = "step_id")
    private Long stepId;

    @Column(name = "seq_order")
    private Integer seqOrder;

//    @ManyToOne
//    @JoinColumn(name = "route_id")
//    private Route route;
//
//    @ManyToOne
//    @JoinColumn(name = "step_id")
//    private Step step;
//
//    @Column(name = "seq_order")
//    private Integer seqOrder;
}
