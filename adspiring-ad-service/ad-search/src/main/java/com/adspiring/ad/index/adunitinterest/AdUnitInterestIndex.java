package com.adspiring.ad.index.adunitinterest;


import com.adspiring.ad.index.IndexAware;
import com.adspiring.ad.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

@Slf4j
@Component
public class AdUnitInterestIndex implements IndexAware<String, Set<Long>> {

    // inverted index <tag, set<adUnitId>
    private static Map<String, Set<Long>> interestUnitMap;

    // <unitId, set<tag>
    private static Map<Long, Set<String>> unitInterestMap;

    static {
        interestUnitMap = new ConcurrentHashMap<>();
        unitInterestMap = new ConcurrentHashMap<>();
    }


    @Override
    public Set<Long> get(String key) {
        return interestUnitMap.get(key);
    }

    @Override
    public void add(String key, Set<Long> value) {

        log.info("unitInterestMap, before add: {}", unitInterestMap);
        log.info("interestUnitMap, before add: {}", interestUnitMap);

        Set<Long> unitIds = CommonUtils.getOrCreate(
                key, interestUnitMap,
                ConcurrentSkipListSet::new
        );
        unitIds.addAll(value);

        for (Long unitId : value) {
            Set<String> tags = CommonUtils.getOrCreate(
                    unitId, unitInterestMap,
                    ConcurrentSkipListSet::new
            );
            tags.add(key);
        }

        log.info("unitInterestMap, after add: {}", unitInterestMap);
        log.info("interestUnitMap, after add: {}", interestUnitMap);
    }

    @Override
    public void update(String key, Set<Long> value) {

        log.error("AdUnitInterestIndex don't support update because it is expensive");
        // Update index is more expensive
        // Better approach is to just delete the index, and create a new one
    }

    @Override
    public void delete(String key, Set<Long> value) {

        log.info("unitInterestMap, before delete: {}", unitInterestMap);
        log.info("interestUnitMap, before delete: {}", interestUnitMap);

        Set<Long> unitIds = CommonUtils.getOrCreate(
                key, interestUnitMap,
                ConcurrentSkipListSet::new
        );
        unitIds.removeAll(value);

        for (Long unitId : value) {
            Set<String> tags = CommonUtils.getOrCreate(
                    unitId, unitInterestMap,
                    ConcurrentSkipListSet::new
            );
            tags.remove(key);
        }


        log.info("unitInterestMap, after delete: {}", unitInterestMap);
        log.info("interestUnitMap, after delete: {}", interestUnitMap);

    }

    public boolean match(Long unitId, List<String> tags) {

        if (unitInterestMap.containsKey(unitId)
                && CollectionUtils.isNotEmpty(unitInterestMap.get(unitId))
        ) {
            Set<String> unitInterestTags = unitInterestMap.get(unitId);

            // NOTE: check if tags is sub-collection of unitInterestTags
            return CollectionUtils.isSubCollection(tags, unitInterestTags);
        }
        return false;
    }

}
