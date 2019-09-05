package com.adspiring.ad.index.adcreativeunit;


import com.adspiring.ad.index.IndexAware;
import com.adspiring.ad.index.adunit.AdUnitObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;


@Slf4j
@Component
public class AdCreativeUnitIndex implements IndexAware<String, AdCreativeUnitObject> {

    // <key,value> = <creativeId-unitId, CreativeUnitObject>
    private static Map<String, AdCreativeUnitObject> objectMap;
    // <key,value> = <creativeId, Set<unitId>
    private static Map<Long, Set<Long>> creativeUnitMap;
    // <key,value> = <unitId, Set<creativeId>
    private static Map<Long, Set<Long>> unitCreativeMap;

    static {
        objectMap = new ConcurrentHashMap<>();
        creativeUnitMap = new ConcurrentHashMap<>();
        unitCreativeMap = new ConcurrentHashMap<>();
    }

    @Override
    public AdCreativeUnitObject get(String key) {
        return objectMap.get(key);
    }

    @Override
    public void add(String key, AdCreativeUnitObject value) {

        log.info("AdCreativeUnitIndex: objectMap before add: {}", objectMap);
        log.info("AdCreativeUnitIndex: creativeUnitMap before add: {}", creativeUnitMap);
        log.info("AdCreativeUnitIndex: unitCreativeMap before add: {}", unitCreativeMap);

        // add in objectMap
        objectMap.put(key, value);

        // add in creativeUnitMap
        Set<Long> unitIds = creativeUnitMap.get(value.getCreativeId());
        if (CollectionUtils.isEmpty(unitIds)) {
            unitIds = new ConcurrentSkipListSet<>();
            creativeUnitMap.put(value.getCreativeId(), unitIds);
        }
        unitIds.add(value.getUnitId());

        // add in unitCreativeMap
        Set<Long> creativeIds = unitCreativeMap.get(value.getUnitId());
        if (CollectionUtils.isEmpty(creativeIds)) {
            creativeIds = new ConcurrentSkipListSet<>();
            unitCreativeMap.put(value.getUnitId(), creativeIds);
        }
        creativeIds.add(value.getUnitId());

        log.info("AdCreativeUnitIndex: objectMap after add: {}", objectMap);
        log.info("AdCreativeUnitIndex: creativeUnitMap after add: {}", creativeUnitMap);
        log.info("AdCreativeUnitIndex: unitCreativeMap after add: {}", unitCreativeMap);

    }

    @Override
    public void update(String key, AdCreativeUnitObject value) {

        log.error("AdCreativeUnitIndex don't support update because it is expensive");
        // Update index is more expensive
        // Better approach is to just delete the index, and create a new one

    }

    @Override
    public void delete(String key, AdCreativeUnitObject value) {

        log.info("AdCreativeUnitIndex: objectMap before add: {}", objectMap);
        log.info("AdCreativeUnitIndex: creativeUnitMap before add: {}", creativeUnitMap);
        log.info("AdCreativeUnitIndex: unitCreativeMap before add: {}", unitCreativeMap);

        // delete in objectMap
        objectMap.remove(key);

        // delete in creativeUnitMap
        Set<Long> unitIds = creativeUnitMap.get(value.getCreativeId());
        if (CollectionUtils.isNotEmpty(unitIds)) {
            unitIds.remove(value.getUnitId());
        }

        // delete in unitCreativeMap
        Set<Long> creativeIds = unitCreativeMap.get(value.getUnitId());
        if (CollectionUtils.isNotEmpty(creativeIds)) {
            creativeIds.remove(value.getCreativeId());
        }

        log.info("AdCreativeUnitIndex: objectMap after add: {}", objectMap);
        log.info("AdCreativeUnitIndex: creativeUnitMap after add: {}", creativeUnitMap);
        log.info("AdCreativeUnitIndex: unitCreativeMap after add: {}", unitCreativeMap);

    }

    public List<Long> selectCreativeAds(List<AdUnitObject> unitObjects) {

        if (CollectionUtils.isEmpty(unitObjects)) {
            return Collections.emptyList();
        }

        List<Long> result = new ArrayList<>();

        for (AdUnitObject unitObject : unitObjects) {

            Set<Long> creativeIds = unitCreativeMap.get(unitObject.getUnitId());
            if (CollectionUtils.isNotEmpty(creativeIds)) {
                result.addAll(creativeIds);
            }
        }

        return result;

    }


}
