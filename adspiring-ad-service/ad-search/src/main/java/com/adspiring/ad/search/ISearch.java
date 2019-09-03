package com.adspiring.ad.search;

import com.adspiring.ad.search.vo.SearchRequest;
import com.adspiring.ad.search.vo.SearchResponse;

public interface ISearch {

    SearchResponse fetchAds(SearchRequest request);
}
