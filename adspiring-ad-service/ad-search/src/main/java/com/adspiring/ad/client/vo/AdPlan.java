package com.adspiring.ad.client.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdPlan {

    private Long id;
    private List<Long> userId;
    private String planName;
    private String planStatus;
    private Date startDate;
    private Date endDate;
    private Date createTime;
    private Date updateTime;


}
