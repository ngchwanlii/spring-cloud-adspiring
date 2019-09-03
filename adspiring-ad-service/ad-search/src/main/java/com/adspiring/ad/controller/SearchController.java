package com.adspiring.ad.controller;

import com.adspiring.ad.annotation.IgnoreResponseAdvice;
import com.adspiring.ad.client.vo.AdPlan;
import com.adspiring.ad.client.vo.AdPlanGetRequest;
import com.adspiring.ad.search.ISearch;
import com.adspiring.ad.search.vo.SearchRequest;
import com.adspiring.ad.search.vo.SearchResponse;
import com.adspiring.ad.vo.CommonResponse;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@RestController
public class SearchController {

    private final ISearch search;

    private final RestTemplate restTemplate;

    @Autowired
    public SearchController(RestTemplate restTemplate, ISearch search) {
        this.restTemplate = restTemplate;
        this.search = search;
    }

    @PostMapping("/fetchAds")
    public SearchResponse fetchAds(@RequestBody SearchRequest request) {

        log.info("ad-search: fetchAds -> {}",
                JSON.toJSONString(request));
        return search.fetchAds(request);
    }

    @SuppressWarnings("all")
    @IgnoreResponseAdvice
    @PostMapping("/getAdPlansByRibbon")
    public CommonResponse<List<AdPlan>> getAdPlansByRibbon(
            @RequestBody AdPlanGetRequest request) {

        log.info("ad-search: getAdPlansByRibbon -> {}",
                JSON.toJSONString(request));

        return restTemplate.postForEntity(
                "http://eureka-client-ad-sponsor/ad-sponsor/get/adPlan",
                request,
                CommonResponse.class
        ).getBody();
    }

}
