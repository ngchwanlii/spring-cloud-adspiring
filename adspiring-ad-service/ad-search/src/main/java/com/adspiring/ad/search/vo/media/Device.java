package com.adspiring.ad.search.vo.media;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Device {

    // physical device (need their ip, mac address etc..)

    // device id
    private String deviceCode;
    // device mac address
    private String mac;

    private String ip;

    private String model;

    private String displaySize;

    private String screenSize;

    // device serial number or name
    private String serialName;

}
