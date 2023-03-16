package com.vo;

import lombok.Data;

@Data
public class UserFormVO {
    private Long uid;

    private String oldPassword;

    private String newPassword;
}
