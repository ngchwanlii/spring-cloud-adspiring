package com.adspiring.ad.service;

import com.adspiring.ad.entity.AdPlan;
import com.adspiring.ad.exception.AdException;
import com.adspiring.ad.vo.AdPlanGetRequest;
import com.adspiring.ad.vo.AdPlanRequest;
import com.adspiring.ad.vo.AdPlanResponse;

import java.util.List;

public interface IAdPlanService {

    AdPlanResponse createAdPlan(AdPlanRequest request) throws AdException;

    List<AdPlan> getAdPlanByIds(AdPlanGetRequest request) throws AdException;

    AdPlanResponse updateAdPlan(AdPlanRequest request) throws AdException;

    void deleteAdPlan(AdPlanRequest request) throws AdException;

}
