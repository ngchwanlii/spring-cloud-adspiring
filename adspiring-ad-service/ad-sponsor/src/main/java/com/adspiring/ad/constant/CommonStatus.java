package com.adspiring.ad.constant;

import lombok.Getter;

@Getter
public enum CommonStatus {

    VALID(1, "valid"),
    INVALID(0, "invalid");

    private Integer status;
    private String desc;

    CommonStatus(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

}
