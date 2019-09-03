package com.adspiring.ad.sender;

import com.adspiring.ad.mysql.dto.MySqlRowData;

public interface ISender {

    // for building incremental index
    void sender(MySqlRowData rowData);

}
