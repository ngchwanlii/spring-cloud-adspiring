package com.adspiring.ad.index.adunitdistrict;

import com.adspiring.ad.index.IndexAware;
import com.adspiring.ad.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

@Slf4j
@Component
public class AdUnitDistrictIndex implements IndexAware<String, Set<Long>> {

    private static Map<String, Set<Long>> districtUnitMap;
    private static Map<Long, Set<String>> unitDistrictMap;

    static {
        districtUnitMap = new ConcurrentHashMap<>();
        unitDistrictMap = new ConcurrentHashMap<>();
    }

    @Override
    public Set<Long> get(String key) {
        return districtUnitMap.get(key);
    }

    @Override
    public void add(String key, Set<Long> value) {

        log.info("districtUnitMap , before add {}", districtUnitMap);
        log.info("unitDistrictMap , before add {}", unitDistrictMap);

        Set<Long> unitIds = CommonUtils.getOrCreate(
                key, districtUnitMap,
                ConcurrentSkipListSet::new
        );
        unitIds.addAll(value);

        for (Long unitId : value) {

            Set<String> districts = CommonUtils.getOrCreate(
                    unitId, unitDistrictMap,
                    ConcurrentSkipListSet::new
            );
            districts.add(key);
        }

        log.info("districtUnitMap , after add {}", districtUnitMap);
        log.info("unitDistrictMap , after add {}", unitDistrictMap);

    }

    @Override
    public void update(String key, Set<Long> value) {

        log.error("AdUnitDistrictIndex don't support update because it is expensive");
        // Update index is more expensive
        // Better approach is to just delete the index, and create a new one
    }

    @Override
    public void delete(String key, Set<Long> value) {

        log.info("districtUnitMap , before delete {}", districtUnitMap);
        log.info("unitDistrictMap , before delete {}", unitDistrictMap);

        Set<Long> unitIds = CommonUtils.getOrCreate(
                key, districtUnitMap,
                ConcurrentSkipListSet::new
        );
        unitIds.removeAll(value);

        for (Long unitId : value) {

            Set<String> districts = CommonUtils.getOrCreate(
                    unitId, unitDistrictMap,
                    ConcurrentSkipListSet::new
            );
            districts.remove(key);
        }

        log.info("districtUnitMap , after delete {}", districtUnitMap);
        log.info("unitDistrictMap , after delete {}", unitDistrictMap);

    }
}
