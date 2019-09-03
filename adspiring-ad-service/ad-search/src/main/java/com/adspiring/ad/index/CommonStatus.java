package com.adspiring.ad.index;

import lombok.Getter;

@Getter
public enum CommonStatus {

    INVALID(0, "INVALID"),
    VALID(1, "VALID");

    private Integer status;
    private String desc;


    CommonStatus(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }


}
