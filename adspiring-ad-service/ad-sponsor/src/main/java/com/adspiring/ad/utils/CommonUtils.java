package com.adspiring.ad.utils;

import com.adspiring.ad.exception.AdException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.time.DateUtils;

import java.util.Date;

public class CommonUtils {

    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy/MM/dd", "yyyy.MM.dd"
    };

    public static String sha256(String value) {
        return DigestUtils.sha256Hex(value).toUpperCase();
    }

    public static Date parseStringDate(String stringDate)
            throws AdException {
        try {
            return DateUtils.parseDate(
                    stringDate,
                    parsePatterns
            );
        } catch (Exception ex) {
            throw new AdException(ex.getMessage());
        }
    }

}
