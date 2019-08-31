package com.adspiring.ad.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class CommonUtils {

    public static String sha256(String value) {
        return DigestUtils.sha256Hex(value).toUpperCase();
    }

}
