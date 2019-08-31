package com.adspiring.ad.service.impl;

import com.adspiring.ad.constant.CommonStatus;
import com.adspiring.ad.constant.Constants;
import com.adspiring.ad.dao.AdPlanRepository;
import com.adspiring.ad.dao.AdUserRepository;
import com.adspiring.ad.entity.AdPlan;
import com.adspiring.ad.entity.AdUser;
import com.adspiring.ad.exception.AdException;
import com.adspiring.ad.service.IAdPlanService;
import com.adspiring.ad.utils.CommonUtils;
import com.adspiring.ad.vo.AdPlanGetRequest;
import com.adspiring.ad.vo.AdPlanRequest;
import com.adspiring.ad.vo.AdPlanResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AdPlanServiceImpl implements IAdPlanService {

    private final AdUserRepository userRepository;
    private final AdPlanRepository planRepository;

    @Autowired
    public AdPlanServiceImpl(AdUserRepository userRepository, AdPlanRepository planRepository) {
        this.userRepository = userRepository;
        this.planRepository = planRepository;
    }

    @Override
    @Transactional
    public AdPlanResponse createAdPlan(AdPlanRequest request) throws AdException {

        if (!request.createValidate()) {
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }

        // identify related user_id exist
        Optional<AdUser> adUser = userRepository.findById(request.getUserId());
        if (!adUser.isPresent()) {
            throw new AdException(Constants.ErrorMsg.CANNOT_FIND_RECORD);
        }

        AdPlan adPlan = planRepository.findByUserIdAndPlanName(
                request.getUserId(),
                request.getPlanName()
        );

        if (adPlan != null) {
            throw new AdException(Constants.ErrorMsg.DUPLICATE_PLAN_NAME_ERROR);
        }

        adPlan = planRepository.save(
                new AdPlan(
                        request.getUserId(),
                        request.getPlanName(),
                        CommonUtils.parseStringDate(request.getStartDate()),
                        CommonUtils.parseStringDate(request.getEndDate())
                )
        );

        return new AdPlanResponse(
                adPlan.getId(),
                adPlan.getPlanName()
        );
    }

    @Override
    public List<AdPlan> getAdPlanByIds(AdPlanGetRequest request) throws AdException {

        if (!request.validate()) {
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }

        return planRepository.findAllByIdInAndUserId(
                request.getIds(),
                request.getUserId()
        );
    }

    @Override
    @Transactional
    public AdPlanResponse updateAdPlan(AdPlanRequest request) throws AdException {


        if (!request.updateValidate()) {
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }

        AdPlan adPlan = planRepository.findByIdAndUserId(
                request.getId(),
                request.getUserId()
        );

        if (adPlan == null) {
            throw new AdException(Constants.ErrorMsg.CANNOT_FIND_RECORD);
        }

        if (request.getPlanName() != null) {
            adPlan.setPlanName(request.getPlanName());
        }

        if (request.getStartDate() != null) {
            adPlan.setStartDate(CommonUtils.parseStringDate(request.getStartDate()));
        }

        if (request.getEndDate() != null) {
            adPlan.setEndDate(CommonUtils.parseStringDate(request.getEndDate()));
        }

        adPlan.setUpdateTime(new Date());

        adPlan = planRepository.save(adPlan);

        return new AdPlanResponse(adPlan.getId(), adPlan.getPlanName());
    }

    @Override
    @Transactional
    public void deleteAdPlan(AdPlanRequest request) throws AdException {

        if (!request.deleteValidate()) {
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }

        AdPlan adPlan = planRepository.findByIdAndUserId(
                request.getId(),
                request.getUserId()
        );

        if (adPlan == null) {
            throw new AdException(Constants.ErrorMsg.CANNOT_FIND_RECORD);
        }

        adPlan.setPlanStatus(CommonStatus.INVALID.getStatus());
        adPlan.setUpdateTime(new Date());
        planRepository.save(adPlan);

    }
}
