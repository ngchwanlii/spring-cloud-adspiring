package com.adspiring.ad.mysql.listener;


import com.adspiring.ad.mysql.constant.Constant;
import com.adspiring.ad.mysql.constant.OpType;
import com.adspiring.ad.mysql.dto.BinlogRowData;
import com.adspiring.ad.mysql.dto.MySqlRowData;
import com.adspiring.ad.mysql.dto.TableTemplate;
import com.adspiring.ad.sender.ISender;
import com.github.shyiko.mysql.binlog.event.EventType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class IncrementListener implements Ilistener {

    // send to Kafka / incrementing index etc..
    @Resource(name = "")
    private ISender sender;

    private final AggregationListener aggregationListener;

    @Autowired
    public IncrementListener(AggregationListener aggregationListener) {
        this.aggregationListener = aggregationListener;
    }

    @Override
    @PostConstruct
    public void register() {
        log.info("IncrementListener register db and table info");
        Constant.table2Db.forEach((tableName, dbName) ->
                aggregationListener.register(dbName, tableName, this));
    }

    @Override
    public void onEvent(BinlogRowData eventData) {

        TableTemplate table = eventData.getTable();
        EventType eventType = eventData.getEventType();

        // encapsulate as delivery data
        MySqlRowData rowData = new MySqlRowData();

        rowData.setTableName(table.getTableName());
        rowData.setLevel(table.getLevel());
        OpType opType = OpType.to(eventType);
        rowData.setOpType(opType);

        // get template
        List<String> fieldList = table.getOpTypeFieldSetMap().get(opType);

        if (fieldList == null) {
            log.warn("{} not support for {}", opType, table.getTableName());
            return;
        }

        // the column data that has been modified
        for (Map<String, String> afterMap : eventData.getAfter()) {
            Map<String, String> _afterMap = new HashMap<>();
            for (Map.Entry<String, String> entry : afterMap.entrySet()) {

                String colName = entry.getKey();
                String colValue = entry.getValue();
                _afterMap.put(colName, colValue);
            }
            rowData.getFieldValueMap().add(_afterMap);
        }

        sender.sender(rowData);
    }
}
