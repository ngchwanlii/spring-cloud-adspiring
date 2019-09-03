package com.adspiring.ad.search.vo.media;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Geo {

    private Float latitude;
    private Float longitude;

    private String state;
    private String city;

}
