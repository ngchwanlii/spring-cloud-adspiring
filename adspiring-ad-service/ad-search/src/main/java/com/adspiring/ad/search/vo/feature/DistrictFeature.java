package com.adspiring.ad.search.vo.feature;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistrictFeature {

    private List<StateAndCity> districts;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StateAndCity {
        private String state;
        private String city;
    }

}
