package com.adspiring.ad.index.adcreative;

import com.adspiring.ad.index.IndexAware;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class AdCreativeIndex implements IndexAware<Long, AdCreativeObject> {

    private static Map<Long, AdCreativeObject> objectMap;

    static {
        objectMap = new ConcurrentHashMap<>();
    }

    @Override
    public AdCreativeObject get(Long key) {
        return objectMap.get(key);
    }

    @Override
    public void add(Long key, AdCreativeObject value) {

        log.info("AdCreativeIndex, before add {}", objectMap);
        objectMap.put(key, value);
        log.info("AdCreativeIndex, after add {}", objectMap);

    }

    @Override
    public void update(Long key, AdCreativeObject value) {

        log.info("AdCreativeIndex, before update {}", objectMap);

        AdCreativeObject oldObject = objectMap.get(key);
        if (oldObject == null) {
            objectMap.put(key, value);
        } else {
            oldObject.update(value);
        }

        log.info("AdCreativeIndex, after update {}", objectMap);

    }

    @Override
    public void delete(Long key, AdCreativeObject value) {

        log.info("AdCreativeIndex, before delete {}", objectMap);
        objectMap.remove(key);
        log.info("AdCreativeIndex, before delete {}", objectMap);

    }
}
