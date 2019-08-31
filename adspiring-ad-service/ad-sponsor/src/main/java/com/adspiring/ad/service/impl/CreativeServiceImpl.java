package com.adspiring.ad.service.impl;

import com.adspiring.ad.dao.CreativeRepository;
import com.adspiring.ad.entity.Creative;
import com.adspiring.ad.service.ICreativeService;
import com.adspiring.ad.vo.CreativeRequest;
import com.adspiring.ad.vo.CreativeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreativeServiceImpl implements ICreativeService {

    private final CreativeRepository creativeRepository;

    @Autowired
    public CreativeServiceImpl(CreativeRepository creativeRepository) {
        this.creativeRepository = creativeRepository;
    }

    @Override
    public CreativeResponse createCreative(CreativeRequest request) {

        Creative creative = creativeRepository.save(
          request.convertToEntity()
        );

        return new CreativeResponse(creative.getId(), creative.getName());
    }
}
