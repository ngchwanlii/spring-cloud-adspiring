package com.adspiring.ad.search.impl;

import com.adspiring.ad.index.CommonStatus;
import com.adspiring.ad.index.DataTable;
import com.adspiring.ad.index.adcreative.AdCreativeIndex;
import com.adspiring.ad.index.adcreative.AdCreativeObject;
import com.adspiring.ad.index.adcreativeunit.AdCreativeUnitIndex;
import com.adspiring.ad.index.adunit.AdUnitIndex;
import com.adspiring.ad.index.adunit.AdUnitObject;
import com.adspiring.ad.index.adunitdistrict.AdUnitDistrictIndex;
import com.adspiring.ad.index.adunitinterest.AdUnitInterestIndex;
import com.adspiring.ad.index.adunitkeyword.AdUnitKeywordIndex;
import com.adspiring.ad.search.ISearch;
import com.adspiring.ad.search.vo.SearchRequest;
import com.adspiring.ad.search.vo.SearchResponse;
import com.adspiring.ad.search.vo.feature.DistrictFeature;
import com.adspiring.ad.search.vo.feature.FeatureRelation;
import com.adspiring.ad.search.vo.feature.InterestFeature;
import com.adspiring.ad.search.vo.feature.KeywordFeature;
import com.adspiring.ad.search.vo.media.AdSlot;
import com.alibaba.fastjson.JSON;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class SearchImpl implements ISearch {

    public SearchResponse fallback(SearchRequest request, Throwable e) {
        return null;
    }

    @Override
    @HystrixCommand(fallbackMethod = "fallback")
    public SearchResponse fetchAds(SearchRequest request) {

        // retrieve requested ads slot
        List<AdSlot> adSlots = request.getRequestInfo().getAdSlots();

        // features in FeatureInfo
        KeywordFeature keywordFeature = request.getFeatureInfo().getKeywordFeature();
        InterestFeature interestFeature = request.getFeatureInfo().getInterestFeature();
        DistrictFeature districtFeature = request.getFeatureInfo().getDistrictFeature();


        // Match all features (AND) or either one (OR)
        FeatureRelation relation = request.getFeatureInfo().getRelation();

        // construct response
        SearchResponse response = new SearchResponse();
        Map<String, List<SearchResponse.Creative>> adSlot2Ads =
                response.getAdSlot2Ads();

        for (AdSlot adSlot : adSlots) {

            Set<Long> targetUnitIdSet;

            /**
             * 1. Screening procedure
             *
             * Get AdUnit based on Ads position type (e.g: Screen, Card, Card Middle, Ads Card
             * while video pausing etc..)
             */
            Set<Long> adUnitIdSet = DataTable.of(
                    AdUnitIndex.class
            ).match(adSlot.getPositionType());


            /**
             *  2. Filtering process based on FeatureInfo
             *      (e.g: keywords, district, interest)
             */
            if (relation == FeatureRelation.AND) {

                filterKeywordFeature(adUnitIdSet, keywordFeature);
                filterDistrictFeature(adUnitIdSet, districtFeature);
                filterInterestTagFeature(adUnitIdSet, interestFeature);

                targetUnitIdSet = adUnitIdSet;

            } else {

                targetUnitIdSet = getOrRelationUnitIds(
                        adUnitIdSet,
                        keywordFeature,
                        districtFeature,
                        interestFeature
                );
            }


            /**
             *  3. Filtering process based on Status
             *      (e.g: VALID / INVALID)
             */
            List<AdUnitObject> unitObjects = DataTable.of(AdUnitIndex.class)
                    .fetch(targetUnitIdSet);

            filterAdUnitAndPlanStatus(unitObjects, CommonStatus.VALID);

            /**
             *  4. Filtering process based on AdSlot info (e.g: width, height, type)
             *
             */
            List<Long> adCreativeIds = DataTable.of(AdCreativeUnitIndex.class)
                    .selectCreativeAds(unitObjects);

            List<AdCreativeObject> creatives = DataTable.of(AdCreativeIndex.class)
                    .fetch(adCreativeIds);

            filterCreativeByAdSlot(
                    creatives,
                    adSlot.getWidth(),
                    adSlot.getHeight(),
                    adSlot.getType()
            );

            /**
             * 5. Finally, select the corresponding Ad-creatives (ads),
             * convert it to creativeResponse
             * put it into adSlot2Ads map (key: adSlotCode, value: List<Creative>)
             *
             */
            adSlot2Ads.put(
                    adSlot.getAdSlotCode(),
                    buildCreativeResponse(creatives)
            );
        }

        log.info("fetchAds: {}-{}",
                JSON.toJSONString(request),
                JSON.toJSONString(response));

        return response;
    }

    private Set<Long> getOrRelationUnitIds(Set<Long> adUnitIdSet,
                                           KeywordFeature keywordFeature,
                                           DistrictFeature districtFeature,
                                           InterestFeature interestFeature) {
        if (CollectionUtils.isEmpty(adUnitIdSet)) {
            return Collections.emptySet();
        }

        Set<Long> keywordUnitIdSet = new HashSet<>(adUnitIdSet);
        Set<Long> districtUnitIdSet = new HashSet<>(adUnitIdSet);
        Set<Long> interestUnitIdSet = new HashSet<>(adUnitIdSet);

        filterKeywordFeature(keywordUnitIdSet, keywordFeature);
        filterDistrictFeature(districtUnitIdSet, districtFeature);
        filterInterestTagFeature(interestUnitIdSet, interestFeature);

        return new HashSet<>(
                CollectionUtils.union(
                        CollectionUtils.union(districtUnitIdSet, interestUnitIdSet),
                        keywordUnitIdSet
                )
        );

    }

    private void filterKeywordFeature(Collection<Long> adUnitIds,
                                      KeywordFeature keywordFeature) {

        if (CollectionUtils.isEmpty(adUnitIds)) {
            return;
        }

        if (CollectionUtils.isNotEmpty(keywordFeature.getKeywords())) {
            CollectionUtils.filter(
                    adUnitIds,
                    adUnitId -> DataTable.of(AdUnitKeywordIndex.class).
                            match(adUnitId, keywordFeature.getKeywords())
            );
        }
    }

    private void filterDistrictFeature(Collection<Long> adUnitIds,
                                       DistrictFeature districtFeature) {

        if (CollectionUtils.isEmpty(adUnitIds)) {
            return;
        }

        if (CollectionUtils.isNotEmpty(districtFeature.getDistricts())) {
            CollectionUtils.filter(
                    adUnitIds,
                    adUnitId -> DataTable.of(AdUnitDistrictIndex.class).
                            match(adUnitId, districtFeature.getDistricts())
            );
        }

    }

    private void filterInterestTagFeature(Collection<Long> adUnitIds,
                                          InterestFeature interestFeature) {

        if (CollectionUtils.isEmpty(adUnitIds)) {
            return;
        }

        if (CollectionUtils.isNotEmpty(interestFeature.getInterestTags())) {
            CollectionUtils.filter(
                    adUnitIds,
                    adUnitId -> DataTable.of(AdUnitInterestIndex.class).
                            match(adUnitId, interestFeature.getInterestTags())
            );
        }
    }

    private void filterAdUnitAndPlanStatus(List<AdUnitObject> unitObjects,
                                           CommonStatus status) {

        if (CollectionUtils.isEmpty(unitObjects)) {
            return;
        }

        CollectionUtils.filter(
                unitObjects,
                object -> object.getUnitStatus().equals(status.getStatus())
                        && object.getAdPlanObject().getPlanStatus().equals(status.getStatus())
        );

    }

    private void filterCreativeByAdSlot(List<AdCreativeObject> creatives,
                                        Integer width,
                                        Integer height,
                                        List<Integer> type) {

        if (CollectionUtils.isEmpty(creatives)) {
            return;
        }

        CollectionUtils.filter(
                creatives,
                creative -> creative.getAuditStatus().equals(
                        CommonStatus.VALID.getStatus())
                        && creative.getWidth().equals(width)
                        && creative.getHeight().equals(height)
                        && type.contains(creative.getType())
        );

    }

    private List<SearchResponse.Creative> buildCreativeResponse(
            List<AdCreativeObject> creatives) {
        if (CollectionUtils.isEmpty(creatives)) {
            return Collections.emptyList();
        }

        AdCreativeObject randomObject = creatives.get(
                Math.abs(new Random().nextInt()) % creatives.size()
        );

        return Collections.singletonList(
                SearchResponse.convert(randomObject)
        );

    }

}
