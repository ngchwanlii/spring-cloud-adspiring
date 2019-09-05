package com.adspiring.ad.search.vo;


import com.adspiring.ad.index.adcreative.AdCreativeObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponse {

    public Map<String, List<Creative>> adSlot2Ads = new HashMap<>();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Creative {

        private Long adId;
        private String adUrl;
        private Integer width;
        private Integer height;
        private Integer type;
        private Integer materialType;

        /**
         * Display monitoring url
         * <p>
         * When requested advertisement return back to media,
         * media will display (expose) these advertisement
         * These display message / information will be deliver to the showMonitorUrl
         * to show that the advertisement has already displayed
         */
        private List<String> showMonitorUrl =
                Arrays.asList("www.google.com", "www.google.com");


        /**
         * Display clicked url
         * <p>
         * clickMonitorUrl
         * The media will be displaying the advertisement,
         * and the end client can click this url to navigate to
         * advertisement source (e.g: webpage / mobile page / app store / google store etc..)
         */
        private List<String> clickMonitorUrl =
                Arrays.asList("www.google.com", "www.google.com");

    }

    public static Creative convert(AdCreativeObject object) {

        Creative creative = new Creative();

        creative.setAdId(object.getId());
        creative.setAdUrl(object.getUrl());
        creative.setWidth(object.getWidth());
        creative.setHeight(object.getHeight());
        creative.setType(object.getType());
        creative.setMaterialType(object.getMaterialType());

        return creative;

    }


}
