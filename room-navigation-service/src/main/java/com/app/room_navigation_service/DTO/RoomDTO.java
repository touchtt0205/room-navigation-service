//package com.app.room_navigation_service.DTO;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//public class RoomDTO {
//    private Long id;
//    private String name;
//    private Integer step;
//    private String building;
//    private Integer floor;
//    private String faculty;
//    private Integer routeId;
//    private String room_image_url;
//}

package com.app.room_navigation_service.DTO;

import lombok.Data;

@Data
public class RoomDTO {
    private Integer id;
    private String name;
    private Integer floorId;
    private Boolean hasSharedRoute;
}
