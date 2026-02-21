package com.app.room_navigation_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

@Data
@AllArgsConstructor
@FieldNameConstants
public class AdminDTO {
    private String fullNameEN;
    private String fullNameTH;
    private String itAccount;
    private String facultyId;
    private String facultyNameTH;
    private String facultyNameEN;
}
