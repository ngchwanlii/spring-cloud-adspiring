package com.adspiring.ad.index.adplan;

import com.adspiring.ad.index.IndexAware;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class AdPlanIndex implements IndexAware<Long, AdPlanObject> {

    private static Map<Long, AdPlanObject> objectMap;

    static {
        objectMap = new ConcurrentHashMap<>();
    }

    @Override
    public AdPlanObject get(Long key) {
        return objectMap.get(key);
    }

    @Override
    public void add(Long key, AdPlanObject value) {
        log.info("Before add: {}", objectMap);
        objectMap.put(key, value);
        log.info("After add: {}", objectMap);
    }

    @Override
    public void update(Long key, AdPlanObject value) {

        log.info("Before update: {}", objectMap);

        AdPlanObject oldObject = objectMap.get(key);
        if (oldObject == null) {
            objectMap.put(key, value);
        } else {
            oldObject.update(value);
        }

        log.info("After update: {}", objectMap);
    }

    @Override
    public void delete(Long key, AdPlanObject value) {

        log.info("Before delete: {}", objectMap);
        objectMap.remove(key);
        log.info("After delete: {}", objectMap);

    }
}
