package com.adspiring.ad.controller;


import com.adspiring.ad.entity.AdPlan;
import com.adspiring.ad.exception.AdException;
import com.adspiring.ad.service.IAdPlanService;
import com.adspiring.ad.vo.AdPlanGetRequest;
import com.adspiring.ad.vo.AdPlanRequest;
import com.adspiring.ad.vo.AdPlanResponse;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class AdPlanOPController {

    private final IAdPlanService adPlanService;

    @Autowired
    public AdPlanOPController(IAdPlanService adPlanService) {
        this.adPlanService = adPlanService;
    }

    @PostMapping("/create/adPlan")
    public AdPlanResponse createAdPlan(
            @RequestBody AdPlanRequest request) throws AdException {

        log.info("ad-sponsor: createAdPlan -> {}",
                JSON.toJSONString(request));

        return adPlanService.createAdPlan(request);
    }

    @PostMapping("/get/adPlan")
    public List<AdPlan> getAdPlanByIds(
            @RequestBody AdPlanGetRequest request) throws AdException {

        log.info("ad-sponsor: getAdPlanByIds -> {}",
                JSON.toJSONString(request));

        return adPlanService.getAdPlanByIds(request);
    }

    @PutMapping("/update/adPlan")
    public AdPlanResponse updateAdPlan(
            @RequestBody AdPlanRequest request) throws AdException {

        log.info("ad-sponsor: updateAdPlan -> {}",
                JSON.toJSONString(request));

        return adPlanService.updateAdPlan(request);
    }

    @DeleteMapping("/delete/adPlan")
    public void deleteAdPlan(
            @RequestBody AdPlanRequest request) throws AdException {

        log.info("ad-sponsor: deleteAdPlan -> {}",
                JSON.toJSONString(request));

        adPlanService.deleteAdPlan(request);

    }

}
