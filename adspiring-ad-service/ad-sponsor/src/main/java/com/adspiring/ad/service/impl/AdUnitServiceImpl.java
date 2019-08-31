package com.adspiring.ad.service.impl;

import com.adspiring.ad.constant.Constants;
import com.adspiring.ad.dao.AdPlanRepository;
import com.adspiring.ad.dao.AdUnitRepository;
import com.adspiring.ad.dao.CreativeRepository;
import com.adspiring.ad.dao.unit_condition.AdUnitDistrictRepository;
import com.adspiring.ad.dao.unit_condition.AdUnitInterestRepository;
import com.adspiring.ad.dao.unit_condition.AdUnitKeywordRepository;
import com.adspiring.ad.dao.unit_condition.CreativeUnitRepository;
import com.adspiring.ad.entity.AdPlan;
import com.adspiring.ad.entity.AdUnit;
import com.adspiring.ad.entity.unit_condition.AdUnitDistrict;
import com.adspiring.ad.entity.unit_condition.AdUnitInterest;
import com.adspiring.ad.entity.unit_condition.AdUnitKeyword;
import com.adspiring.ad.entity.unit_condition.CreativeUnit;
import com.adspiring.ad.exception.AdException;
import com.adspiring.ad.service.IAdUnitService;
import com.adspiring.ad.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdUnitServiceImpl implements IAdUnitService {

    private final AdPlanRepository planRepository;
    private final AdUnitRepository unitRepository;
    private final CreativeRepository creativeRepository;
    private final CreativeUnitRepository creativeUnitRepository;

    private final AdUnitKeywordRepository unitKeywordRepository;
    private final AdUnitInterestRepository unitInterestRepository;
    private final AdUnitDistrictRepository unitDistrictRepository;

    @Autowired
    public AdUnitServiceImpl(
            AdPlanRepository planRepository,
            AdUnitRepository unitRepository,
            CreativeRepository creativeRepository,
            CreativeUnitRepository creativeUnitRepository,
            AdUnitKeywordRepository unitKeywordRepository,
            AdUnitInterestRepository unitInterestRepository,
            AdUnitDistrictRepository unitDistrictRepository
    ) {
        this.planRepository = planRepository;
        this.unitRepository = unitRepository;
        this.creativeRepository = creativeRepository;
        this.creativeUnitRepository = creativeUnitRepository;
        this.unitKeywordRepository = unitKeywordRepository;
        this.unitInterestRepository = unitInterestRepository;
        this.unitDistrictRepository = unitDistrictRepository;
    }

    @Override
    public AdUnitResponse createUnit(AdUnitRequest request) throws AdException {

        if (!request.createValidate()) {
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }

        Optional<AdPlan> adPlan = planRepository.findById(request.getPlanId());

        if (!adPlan.isPresent()) {
            throw new AdException(Constants.ErrorMsg.CANNOT_FIND_RECORD);
        }

        AdUnit adUnit = unitRepository.findByPlanIdAndUnitName(
                request.getPlanId(),
                request.getUnitName()
        );

        if (adUnit != null) {
            throw new AdException(Constants.ErrorMsg.DUPLICATE_AD_UNIT_NAME_ERROR);
        }

        adUnit = unitRepository.save(new AdUnit(
                request.getPlanId(),
                request.getUnitName(),
                request.getPositionType(),
                request.getBudget()
        ));

        return new AdUnitResponse(
                adUnit.getId(),
                adUnit.getUnitName()
        );
    }

    @Override
    public AdUnitKeywordResponse createUnitKeyword(AdUnitKeywordRequest request) throws AdException {

        List<Long> unitIds = request.getUnitKeywords().stream()
                .map(AdUnitKeywordRequest.UnitKeyword::getUnitId)
                .collect(Collectors.toList());

        if (!isRelatedUnitExist(unitIds)) {
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }


        List<AdUnitKeyword> unitKeywords = new ArrayList<>();

        request.getUnitKeywords().forEach(i -> unitKeywords.add(
                new AdUnitKeyword(i.getUnitId(), i.getKeyword())
        ));

        List<Long> ids = unitKeywordRepository.saveAll(unitKeywords).stream()
                .map(AdUnitKeyword::getId)
                .collect(Collectors.toList());

        return new AdUnitKeywordResponse(ids);

    }

    @Override
    public AdUnitInterestResponse createUnitInterest(AdUnitInterestRequest request) throws AdException {

        List<Long> unitIds = request.getUnitInterests().stream()
                .map(AdUnitInterestRequest.UnitInterest::getUnitId)
                .collect(Collectors.toList());

        if (!isRelatedUnitExist(unitIds)) {
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }

        List<AdUnitInterest> unitInterests = new ArrayList<>();

        request.getUnitInterests().forEach(i -> unitInterests.add(
                new AdUnitInterest(i.getUnitId(), i.getInterestTag())
        ));

        List<Long> ids = unitInterestRepository.saveAll(unitInterests).stream()
                .map(AdUnitInterest::getId)
                .collect(Collectors.toList());


        return new AdUnitInterestResponse(ids);
    }

    @Override
    public AdUnitDistrictResponse createUnitDistrict(AdUnitDistrictRequest request) throws AdException {

        List<Long> unitIds = request.getUnitDistricts().stream()
                .map(AdUnitDistrictRequest.UnitDistrict::getUnitId)
                .collect(Collectors.toList());

        if (!isRelatedUnitExist(unitIds)) {
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }

        List<AdUnitDistrict> unitDistricts = new ArrayList<>();

        request.getUnitDistricts().forEach(i -> unitDistricts.add(
                new AdUnitDistrict(i.getUnitId(), i.getState(), i.getCity())
        ));

        List<Long> ids = unitDistrictRepository.saveAll(unitDistricts).stream()
                .map(AdUnitDistrict::getId)
                .collect(Collectors.toList());


        return new AdUnitDistrictResponse(ids);
    }

    @Override
    public CreativeUnitResponse createCreativeUnit(CreativeUnitRequest request) throws AdException {


        List<Long> unitIds = request.getCreativeUnitItems().stream()
                .map(CreativeUnitRequest.CreativeUnitItem::getUnitId)
                .collect(Collectors.toList());

        List<Long> creativeIds = request.getCreativeUnitItems().stream()
                .map(CreativeUnitRequest.CreativeUnitItem::getCreativeId)
                .collect(Collectors.toList());

        if (!(isRelatedUnitExist(unitIds) && isRelatedUnitExist(creativeIds))) {
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }

        List<CreativeUnit> creativeUnits = new ArrayList<>();
        request.getCreativeUnitItems().forEach(i -> creativeUnits.add(
                new CreativeUnit(i.getCreativeId(), i.getUnitId())
        ));

        List<Long> ids = creativeUnitRepository.saveAll(creativeUnits)
                .stream()
                .map(CreativeUnit::getCreativeId)
                .collect(Collectors.toList());

        return new CreativeUnitResponse(ids);
    }

    private boolean isRelatedUnitExist(List<Long> unitIds) {

        if (CollectionUtils.isEmpty(unitIds)) {
            return false;
        }
        return unitRepository.findAllById(unitIds).size() == new HashSet<>(unitIds).size();
    }

    private boolean isRelatedCreativeExists(List<Long> creativeIds) {
        if (CollectionUtils.isEmpty(creativeIds)) {
            return false;
        }
        return creativeRepository.findAllById(creativeIds).size() == new HashSet<>(creativeIds).size();
    }

}
