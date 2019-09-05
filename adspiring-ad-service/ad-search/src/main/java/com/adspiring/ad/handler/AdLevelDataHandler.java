package com.adspiring.ad.handler;

import com.adspiring.ad.constant.OpType;
import com.adspiring.ad.dump.table.*;
import com.adspiring.ad.index.DataTable;
import com.adspiring.ad.index.IndexAware;
import com.adspiring.ad.index.adcreative.AdCreativeIndex;
import com.adspiring.ad.index.adcreative.AdCreativeObject;
import com.adspiring.ad.index.adcreativeunit.AdCreativeUnitIndex;
import com.adspiring.ad.index.adcreativeunit.AdCreativeUnitObject;
import com.adspiring.ad.index.adplan.AdPlanIndex;
import com.adspiring.ad.index.adplan.AdPlanObject;
import com.adspiring.ad.index.adunit.AdUnitIndex;
import com.adspiring.ad.index.adunit.AdUnitObject;
import com.adspiring.ad.index.adunitdistrict.AdUnitDistrictIndex;
import com.adspiring.ad.index.adunitinterest.AdUnitInterestIndex;
import com.adspiring.ad.index.adunitkeyword.AdUnitKeywordIndex;
import com.adspiring.ad.utils.CommonUtils;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 1. There are level in between index
 * 2. Full index loading is also part of incremental index with the "add" action
 */
@Slf4j
public class AdLevelDataHandler {

    public static void handleLevel2(AdPlanTable planTable, OpType type) {

        AdPlanObject planObject = new AdPlanObject(
                planTable.getId(),
                planTable.getUserId(),
                planTable.getPlanStatus(),
                planTable.getStartDate(),
                planTable.getEndDate()
        );
        handleBinLogEvent(
                DataTable.of(AdPlanIndex.class),
                planObject.getPlanId(),
                planObject,
                type
        );
    }

    public static void handleLevel2(AdCreativeTable creativeTable, OpType type) {

        AdCreativeObject creativeObject = new AdCreativeObject(
                creativeTable.getId(),
                creativeTable.getName(),
                creativeTable.getType(),
                creativeTable.getMaterialType(),
                creativeTable.getHeight(),
                creativeTable.getWidth(),
                creativeTable.getAuditStatus(),
                creativeTable.getUrl()
        );

        handleBinLogEvent(
                DataTable.of(AdCreativeIndex.class),
                creativeObject.getId(),
                creativeObject,
                type
        );
    }

    public static void handleLevel3(AdUnitTable unitTable, OpType type) {

        AdPlanObject adPlanObject = DataTable.of(
                AdPlanIndex.class
        ).get(unitTable.getPlanId());

        if (adPlanObject == null) {
            log.error("handleLevel3 found AdPlanObject error: {}",
                    unitTable.getPlanId());
            return;
        }

        AdUnitObject unitObject = new AdUnitObject(
                unitTable.getUnitId(),
                unitTable.getUnitStatus(),
                unitTable.getPositionType(),
                unitTable.getPlanId(),
                adPlanObject
        );

        handleBinLogEvent(
                DataTable.of(AdUnitIndex.class),
                unitTable.getUnitId(),
                unitObject,
                type
        );

    }

    public static void handleLevel3(AdCreativeUnitTable creativeUnitTable, OpType type) {
        if (type == OpType.UPDATE) {
            log.error("AdCreativeUnitIndex not support update because it is expensive");
            return;
        }

        AdCreativeObject creativeObject = DataTable.of(
                AdCreativeIndex.class
        ).get(creativeUnitTable.getCreativeId());

        AdUnitObject unitObject = DataTable.of(
                AdUnitIndex.class
        ).get(creativeUnitTable.getUnitId());

        if (unitObject == null || creativeObject == null) {
            log.error("AdCreativeUnitTable index error: {}",
                    JSON.toJSONString(creativeUnitTable));
            return;
        }

        AdCreativeUnitObject creativeUnitObject = new AdCreativeUnitObject(
                creativeUnitTable.getCreativeId(),
                creativeUnitTable.getUnitId()
        );

        handleBinLogEvent(
                DataTable.of(AdCreativeUnitIndex.class),
                CommonUtils.stringConcat(
                        creativeUnitObject.getCreativeId().toString(),
                        creativeUnitObject.getUnitId().toString()
                ),
                creativeUnitObject,
                type
        );

    }

    public static void handleLevel4(AdUnitDistrictTable unitDistrictTable, OpType type) {

        if (type == OpType.UPDATE) {
            log.error("AdUnitDistrictIndex cannot support update");
            return;
        }

        AdUnitObject unitObject = DataTable.of(
                AdUnitIndex.class
        ).get(unitDistrictTable.getUnitId());

        if (unitObject == null) {
            log.error("AdUnitDistrictTable index error: {}",
                    unitDistrictTable.getUnitId());
            return;
        }

        String key = CommonUtils.stringConcat(
                unitDistrictTable.getState(),
                unitDistrictTable.getCity()
        );

        Set<Long> value = new HashSet<>(
                Collections.singleton(unitDistrictTable.getUnitId())
        );

        handleBinLogEvent(
                DataTable.of(AdUnitDistrictIndex.class),
                key,
                value,
                type
        );

    }

    public static void handleLevel4(AdUnitInterestTable unitInterestTable, OpType type) {

        if (type == OpType.UPDATE) {
            log.error("AdUnitInterestIndex cannot support update");
            return;
        }

        AdUnitObject unitObject = DataTable.of(
                AdUnitIndex.class
        ).get(unitInterestTable.getUnitId());

        if (unitObject == null) {
            log.error("AdUnitInterestTable index error: {}",
                    unitInterestTable.getUnitId());
            return;
        }

        Set<Long> value = new HashSet<>(
                Collections.singleton(unitInterestTable.getUnitId())
        );

        handleBinLogEvent(
                DataTable.of(AdUnitInterestIndex.class),
                unitInterestTable.getTag(),
                value,
                type
        );

    }

    public static void handleLevel4(AdUnitKeywordTable unitKeywordTable, OpType type) {

        if (type == OpType.UPDATE) {
            log.error("AdUnitKeywordIndex cannot support update");
            return;
        }

        AdUnitObject unitObject = DataTable.of(
                AdUnitIndex.class
        ).get(unitKeywordTable.getUnitId());

        if (unitObject == null) {
            log.error("AdUnitKeywordTable index error: {}",
                    unitKeywordTable.getUnitId());
            return;
        }

        Set<Long> value = new HashSet<>(
                Collections.singleton(unitKeywordTable.getUnitId())
        );

        handleBinLogEvent(
                DataTable.of(AdUnitKeywordIndex.class),
                unitKeywordTable.getKeyword(),
                value,
                type
        );

    }

    private static <K, V> void handleBinLogEvent(
            IndexAware<K, V> index,
            K key,
            V value,
            OpType type) {

        switch (type) {
            case ADD:
                index.add(key, value);
                break;
            case UPDATE:
                index.update(key, value);
                break;
            case DELETE:
                index.delete(key, value);
                break;
            default:
                break;
        }

    }

}
