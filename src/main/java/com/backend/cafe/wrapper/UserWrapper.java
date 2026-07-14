package com.backend.cafe.wrapper;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserWrapper {

    private Integer id;

    private String name;

    private String username;

    private String mobileNumber;

    private String status;

    private String role;


    public UserWrapper(Integer id, String name, String username, String mobileNumber, String status, String role) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.mobileNumber = mobileNumber;
        this.status = status;
        this.role = role;
    }
}
