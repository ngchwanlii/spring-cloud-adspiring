package com.adspiring.ad.mysql.listener;

import com.adspiring.ad.dto.BinlogRowData;

public interface Ilistener {

    void register();

    void onEvent(BinlogRowData eventData);

}
