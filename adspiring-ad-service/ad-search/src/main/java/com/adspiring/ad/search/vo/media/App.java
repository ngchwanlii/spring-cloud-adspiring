package com.adspiring.ad.search.vo.media;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class App {

    // App: (e.g: the mobile / web / subway advertisement board app etc..)

    // app code
    private String appCode;

    // advertisement pos / flow type
    private String appName;

    private String packageName;

    private String activityName;

}
