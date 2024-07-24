package com.malltail.erp.vo;

import com.malltail.erp.member.Gender;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class MemberResponse {
    private Long id;
    private String longId;
    private String password;
    private String name;
    private Gender gender;
    private LocalDate birthday;
    private Boolean deleteYn;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private String roletype;

    public void clearPassword(){
        this.password = "";
    }

}
