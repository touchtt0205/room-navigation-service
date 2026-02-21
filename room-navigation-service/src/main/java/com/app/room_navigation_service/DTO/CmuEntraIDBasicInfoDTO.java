package com.app.room_navigation_service.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class CmuEntraIDBasicInfoDTO {
    private String cmuitaccount_name;
    private String cmuitaccount;
    private String student_id;
    private String prename_id;
    private String prename_TH;
    private String prename_EN;
    private String firstname_TH;
    private String firstname_EN;
    private String lastname_TH;
    private String lastname_EN;
    private String organization_code;
    private String organization_name_TH;
    private String organization_name_EN;
    private String itaccounttype_id;
    private String itaccounttype_TH;
    private String itaccounttype_EN;
}
