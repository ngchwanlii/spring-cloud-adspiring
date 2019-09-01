package com.adspiring.ad.index.adunitdistrict;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdUnitDistrictObject {

    private Long unitId;
    private String state;
    private String city;

    // <String, Set<Long>>
    // <state-city, Set<Long>>: Combined state-city as one "key"
}
