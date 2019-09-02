package com.adspiring.ad.mysql;

import com.adspiring.ad.mysql.constant.OpType;
import com.adspiring.ad.mysql.dto.ParseTemplate;
import com.adspiring.ad.mysql.dto.TableTemplate;
import com.adspiring.ad.mysql.dto.Template;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class TemplateHolder {

    private ParseTemplate template;

    private final JdbcTemplate jdbcTemplate;

    private String SQL_SCHEMA = "select table_schema, table_name, " +
            "column_name, ordinal_position from information_schema.columns " +
            "where table_schema = ? and table_name = ?";

    @Autowired
    public TemplateHolder(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    private void init() {
        loadJson("template.json");
    }

    public TableTemplate getTable(String tableName) {
        return template.getTableTemplateMap().get(tableName);
    }

    private void loadJson(String path) {

        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        InputStream inStream = cl.getResourceAsStream(path);

        try {
            Template template = JSON.parseObject(
                    inStream,
                    Charset.defaultCharset(),
                    Template.class
            );
            this.template = ParseTemplate.parse(template);
            loadMeta();

        } catch (IOException ex) {
            log.error(ex.getMessage());
            throw new RuntimeException("fail to parse json file");
        }

    }

    private void loadMeta() {

        for (Map.Entry<String, TableTemplate> entry :
                template.getTableTemplateMap().entrySet()) {

            TableTemplate table = entry.getValue();

            List<String> updateFields = table.getOpTypeFieldSetMap().get(
                    OpType.UPDATE
            );

            List<String> insertFields = table.getOpTypeFieldSetMap().get(
                    OpType.ADD
            );

            List<String> deleteFields = table.getOpTypeFieldSetMap().get(
                    OpType.DELETE
            );

            jdbcTemplate.query(SQL_SCHEMA, new Object[]{
                    template.getDatabase(),
                    table.getTableName()
            }, (resultSet, i) -> {
                int pos = resultSet.getInt("ORDINAL_POSITION");
                String colName = resultSet.getString("COLUMN_NAME");

                if ((updateFields != null && updateFields.contains(colName))
                        || (insertFields != null && insertFields.contains(colName))
                        || (deleteFields != null && deleteFields.contains(colName))) {
                    table.getPosMap().put(pos - 1, colName);
                }
                return null;
            });

        }
    }
}
