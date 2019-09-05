package com.adspiring.ad.index;

import com.adspiring.ad.constant.OpType;
import com.adspiring.ad.dump.DConstant;
import com.adspiring.ad.dump.table.*;
import com.adspiring.ad.handler.AdLevelDataHandler;
import com.alibaba.fastjson.JSON;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

// Read <ad-index>.data file and load index
@Component
@DependsOn("dataTable")
public class IndexFileLoader {

    // When Ads Retrieval system launched, do full indexing by loading dumped data
    // NOTE: must be in order <level2, level3, level4>, because there is dependency/level between index
    @PostConstruct
    public void init() {
        List<String> adPlanStrings = loadDumpData(
                String.format("%s%s",
                        DConstant.DATA_ROOT_DIR,
                        DConstant.AD_PLAN)
        );
        adPlanStrings.forEach(p -> AdLevelDataHandler.handleLevel2(
                JSON.parseObject(p, AdPlanTable.class),
                OpType.ADD)
        );

        List<String> adCreativeStrings = loadDumpData(
                String.format("%s%s",
                        DConstant.DATA_ROOT_DIR,
                        DConstant.AD_CREATIVE)
        );
        adCreativeStrings.forEach(c -> AdLevelDataHandler.handleLevel2(
                JSON.parseObject(c, AdCreativeTable.class),
                OpType.ADD
        ));

        List<String> adUnitStrings = loadDumpData(
                String.format("%s%s",
                        DConstant.DATA_ROOT_DIR,
                        DConstant.AD_UNIT)
        );
        adUnitStrings.forEach(u -> AdLevelDataHandler.handleLevel3(
                JSON.parseObject(u, AdUnitTable.class),
                OpType.ADD
        ));

        List<String> adCreativeUnitStrings = loadDumpData(
                String.format("%s%s",
                        DConstant.DATA_ROOT_DIR,
                        DConstant.AD_CREATIVE_UNIT)
        );
        adCreativeUnitStrings.forEach(cu -> AdLevelDataHandler.handleLevel3(
                JSON.parseObject(cu, AdCreativeUnitTable.class),
                OpType.ADD)
        );

        List<String> adUnitDistrictStrings = loadDumpData(
                String.format("%s%s",
                        DConstant.DATA_ROOT_DIR,
                        DConstant.AD_UNIT_DISTRICT)
        );
        adUnitDistrictStrings.forEach(d -> AdLevelDataHandler.handleLevel4(
                JSON.parseObject(d, AdUnitDistrictTable.class),
                OpType.ADD)
        );

        List<String> adUnitInterestStrings = loadDumpData(
                String.format("%s%s",
                        DConstant.DATA_ROOT_DIR,
                        DConstant.AD_UNIT_INTEREST)
        );
        adUnitInterestStrings.forEach(i -> AdLevelDataHandler.handleLevel4(
                JSON.parseObject(i, AdUnitInterestTable.class),
                OpType.ADD)
        );

        List<String> adUnitKeywordStrings = loadDumpData(
                String.format("%s%s",
                        DConstant.DATA_ROOT_DIR,
                        DConstant.AD_UNIT_KEYWORD)
        );
        adUnitKeywordStrings.forEach(k -> AdLevelDataHandler.handleLevel4(
                JSON.parseObject(k, AdUnitKeywordTable.class),
                OpType.ADD)
        );

    }

    private List<String> loadDumpData(String fileName) {
        try (BufferedReader br = Files.newBufferedReader(
                Paths.get(fileName)
        )) {
            return br.lines().collect(Collectors.toList());
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

}
