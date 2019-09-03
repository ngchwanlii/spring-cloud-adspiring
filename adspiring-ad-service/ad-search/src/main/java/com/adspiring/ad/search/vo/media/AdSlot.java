package com.adspiring.ad.search.vo.media;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdSlot {

    // advertisement code
    private String adSlotCode;

    // advertisement pos / flow type
    private Integer positionType;

    private Integer width;
    private Integer height;

    // Advertisement type: (e.g: img, gif, video etc..)
    private List<Integer> type;

    private Integer minCpm;


}
