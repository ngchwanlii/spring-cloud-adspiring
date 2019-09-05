package com.adspiring.ad.sender.index;

import com.adspiring.ad.constant.Constant;
import com.adspiring.ad.dto.MySqlRowData;
import com.adspiring.ad.dump.table.*;
import com.adspiring.ad.handler.AdLevelDataHandler;
import com.adspiring.ad.index.DataLevel;
import com.adspiring.ad.sender.ISender;
import com.adspiring.ad.utils.CommonUtils;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
// for building / updating incremental index by converting mysql data -> <ad-xyz> table
public class IndexSender implements ISender {

    @Override
    public void sender(MySqlRowData rowData) {
        String level = rowData.getLevel();
        if (DataLevel.LEVEL2.getLevel().equals(level)) {
            Level2RowData(rowData);
        } else if (DataLevel.LEVEL3.getLevel().equals(level)) {
            Level3RowData(rowData);
        } else if (DataLevel.LEVEL4.getLevel().equals(level)) {
            Level4RowData(rowData);
        } else {
            log.error("MySqlRowData error: {}", JSON.toJSONString(rowData));
        }
    }

    private void Level2RowData(MySqlRowData rowData) {
        if (rowData.getTableName().equals(
                Constant.AD_PLAN_TABLE_INFO.TABLE_NAME)) {
            List<AdPlanTable> planTables = new ArrayList<>();

            for (Map<String, String> fieldValueMap : rowData.getFieldValueMap()) {
                AdPlanTable planTable = new AdPlanTable();

                fieldValueMap.forEach((k, v) -> {
                    switch (k) {
                        case Constant.AD_PLAN_TABLE_INFO.COLUMN_ID:
                            planTable.setId(Long.valueOf(v));
                            break;
                        case Constant.AD_PLAN_TABLE_INFO.COLUMN_USER_ID:
                            planTable.setUserId(Long.valueOf(v));
                            break;
                        case Constant.AD_PLAN_TABLE_INFO.COLUMN_PLAN_STATUS:
                            planTable.setPlanStatus(Integer.valueOf(v));
                            break;
                        case Constant.AD_PLAN_TABLE_INFO.COLUMN_START_DATE:
                            planTable.setStartDate(CommonUtils.parseStringDate(v));
                            break;
                        case Constant.AD_PLAN_TABLE_INFO.COLUMN_END_DATE:
                            planTable.setEndDate(CommonUtils.parseStringDate(v));
                            break;
                    }
                });
                planTables.add(planTable);
            }

            planTables.forEach(p ->
                    AdLevelDataHandler.handleLevel2(p, rowData.getOpType()));

        } else if (rowData.getTableName().equals(
                Constant.AD_CREATIVE_TABLE_INFO.TABLE_NAME)) {

            List<AdCreativeTable> adCreativeTables = new ArrayList<>();

            for (Map<String, String> fieldValueMap : rowData.getFieldValueMap()) {

                AdCreativeTable adCreativeTable = new AdCreativeTable();

                fieldValueMap.forEach((k, v) -> {

                    switch (k) {
                        case Constant.AD_CREATIVE_TABLE_INFO.COLUMN_ID:
                            adCreativeTable.setId(Long.valueOf(v));
                            break;
                        case Constant.AD_CREATIVE_TABLE_INFO.COLUMN_TYPE:
                            adCreativeTable.setType(Integer.valueOf(v));
                            break;
                        case Constant.AD_CREATIVE_TABLE_INFO.COLUMN_MATERIAL_TYPE:
                            adCreativeTable.setMaterialType(Integer.valueOf(v));
                            break;
                        case Constant.AD_CREATIVE_TABLE_INFO.COLUMN_HEIGHT:
                            adCreativeTable.setHeight(Integer.valueOf(v));
                            break;
                        case Constant.AD_CREATIVE_TABLE_INFO.COLUMN_WIDTH:
                            adCreativeTable.setWidth(Integer.valueOf(v));
                            break;
                        case Constant.AD_CREATIVE_TABLE_INFO.COLUMN_AUDIT_STATUS:
                            adCreativeTable.setAuditStatus(Integer.valueOf(v));
                            break;
                        case Constant.AD_CREATIVE_TABLE_INFO.COLUMN_URL:
                            adCreativeTable.setUrl(v);
                            break;
                    }

                });
                adCreativeTables.add(adCreativeTable);
            }

            adCreativeTables.forEach(c ->
                    AdLevelDataHandler.handleLevel2(c, rowData.getOpType()));

        }
    }

    private void Level3RowData(MySqlRowData rowData) {

        if (rowData.getTableName().equals(
                Constant.AD_UNIT_TABLE_INFO.TABLE_NAME
        )) {
            List<AdUnitTable> unitTables = new ArrayList<>();

            for (Map<String, String> fieldValueMap : rowData.getFieldValueMap()) {

                AdUnitTable unitTable = new AdUnitTable();
                fieldValueMap.forEach((k, v) -> {

                    switch (k) {
                        case Constant.AD_UNIT_TABLE_INFO.COLUMN_ID:
                            unitTable.setUnitId(Long.valueOf(v));
                            break;
                        case Constant.AD_UNIT_TABLE_INFO.COLUMN_UNIT_STATUS:
                            unitTable.setUnitStatus(Integer.valueOf(v));
                            break;
                        case Constant.AD_UNIT_TABLE_INFO.COLUMN_POSITION_TYPE:
                            unitTable.setPositionType(Integer.valueOf(v));
                            break;
                        case Constant.AD_UNIT_TABLE_INFO.COLUMN_PLAN_ID:
                            unitTable.setPlanId(Long.valueOf(v));
                            break;
                    }
                });
                unitTables.add(unitTable);
            }

            unitTables.forEach(u ->
                    AdLevelDataHandler.handleLevel3(u, rowData.getOpType()));

        } else if (rowData.getTableName().equals(
                Constant.AD_CREATIVE_UNIT_TABLE_INFO.TABLE_NAME
        )) {

            List<AdCreativeUnitTable> creativeUnitTables = new ArrayList<>();

            for (Map<String, String> fieldValueMap : rowData.getFieldValueMap()) {

                AdCreativeUnitTable createUnitTable = new AdCreativeUnitTable();

                fieldValueMap.forEach((k, v) -> {
                    switch (k) {
                        case Constant.AD_CREATIVE_UNIT_TABLE_INFO.COLUMN_CREATIVE_ID:
                            createUnitTable.setCreativeId(Long.valueOf(v));
                            break;
                        case Constant.AD_CREATIVE_UNIT_TABLE_INFO.COLUMN_UNIT_ID:
                            createUnitTable.setUnitId(Long.valueOf(v));
                            break;
                    }
                });

                creativeUnitTables.add(createUnitTable);
            }

            creativeUnitTables.forEach(
                    u -> AdLevelDataHandler.handleLevel3(u, rowData.getOpType())
            );
        }

    }

    private void Level4RowData(MySqlRowData rowData) {


        switch (rowData.getTableName()) {
            case Constant.AD_UNIT_DISTRICT_TABLE_INFO.TABLE_NAME:
                List<AdUnitDistrictTable> districtTables = new ArrayList<>();

                for (Map<String, String> fieldValueMap : rowData.getFieldValueMap()) {

                    AdUnitDistrictTable districtTable = new AdUnitDistrictTable();

                    fieldValueMap.forEach((k, v) -> {
                        switch (k) {

                            case Constant.AD_UNIT_DISTRICT_TABLE_INFO.COLUMN_UNIT_ID:
                                districtTable.setUnitId(Long.valueOf(v));
                                break;
                            case Constant.AD_UNIT_DISTRICT_TABLE_INFO.COLUMN_STATE:
                                districtTable.setState(v);
                                break;
                            case Constant.AD_UNIT_DISTRICT_TABLE_INFO.COLUMN_CITY:
                                districtTable.setCity(v);
                                break;
                        }
                    });
                    districtTables.add(districtTable);
                }

                districtTables.forEach(d ->
                        AdLevelDataHandler.handleLevel4(d, rowData
                                .getOpType())
                );
                break;

            case Constant.AD_UNIT_INTEREST_TABLE_INFO.TABLE_NAME:

                List<AdUnitInterestTable> interestTables = new ArrayList<>();

                for (Map<String, String> fieldValueMap : rowData.getFieldValueMap()) {
                    AdUnitInterestTable interestTable = new AdUnitInterestTable();

                    fieldValueMap.forEach((k, v) -> {
                        switch (k) {
                            case Constant.AD_UNIT_INTEREST_TABLE_INFO.COLUMN_UNIT_ID:
                                interestTable.setUnitId(Long.valueOf(v));
                                break;
                            case Constant.AD_UNIT_INTEREST_TABLE_INFO.COLUMN_INTEREST_TAG:
                                interestTable.setTag(v);
                                break;
                        }
                    });

                    interestTables.add(interestTable);
                }

                interestTables.forEach(i ->
                        AdLevelDataHandler.handleLevel4(i, rowData.getOpType()));
                break;

            case Constant.AD_UNIT_KEYWORD_TABLE_INFO.TABLE_NAME:

                List<AdUnitKeywordTable> keywordTables = new ArrayList<>();

                for (Map<String, String> fieldValueMap : rowData.getFieldValueMap()) {
                    AdUnitKeywordTable keywordTable = new AdUnitKeywordTable();

                    fieldValueMap.forEach((k, v) -> {
                        switch (k) {
                            case Constant.AD_UNIT_KEYWORD_TABLE_INFO.COLUMN_UNIT_ID:
                                keywordTable.setUnitId(Long.valueOf(v));
                                break;
                            case Constant.AD_UNIT_KEYWORD_TABLE_INFO.COLUMN_KEYWORD:
                                keywordTable.setKeyword(v);
                                break;
                        }
                    });

                    keywordTables.add(keywordTable);
                }

                keywordTables.forEach(k ->
                        AdLevelDataHandler.handleLevel4(k, rowData.getOpType()));
                break;
        }
    }


}
