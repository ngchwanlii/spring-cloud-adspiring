package com.adspiring.ad.search.vo;


import com.adspiring.ad.search.vo.feature.DistrictFeature;
import com.adspiring.ad.search.vo.feature.FeatureRelation;
import com.adspiring.ad.search.vo.feature.InterestFeature;
import com.adspiring.ad.search.vo.feature.KeywordFeature;
import com.adspiring.ad.search.vo.media.AdSlot;
import com.adspiring.ad.search.vo.media.App;
import com.adspiring.ad.search.vo.media.Device;
import com.adspiring.ad.search.vo.media.Geo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequest {

    // media request id
    private String mediaId;

    private RequestInfo requestInfo;

    private FeatureInfo featureInfo;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestInfo {

        private String requestId;
        private List<AdSlot> adSlots;
        private App app;
        private Geo geo;
        private Device device;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FeatureInfo {

        private KeywordFeature keywordFeature;
        private DistrictFeature districtFeature;
        private InterestFeature interestFeature;

        // set default as: all features must match
        private FeatureRelation relation = FeatureRelation.AND;

    }


}
