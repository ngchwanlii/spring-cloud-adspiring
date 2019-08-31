package com.adspiring.ad.service.impl;

import com.adspiring.ad.constant.Constants;
import com.adspiring.ad.dao.AdUserRepository;
import com.adspiring.ad.entity.AdUser;
import com.adspiring.ad.exception.AdException;
import com.adspiring.ad.service.IUserService;
import com.adspiring.ad.utils.CommonUtils;
import com.adspiring.ad.vo.CreateUserRequest;
import com.adspiring.ad.vo.CreateUserResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    private AdUserRepository userRepository;

    @Autowired
    public UserServiceImpl(AdUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public CreateUserResponse createUser(CreateUserRequest request) throws AdException {

        if (!request.validate()) {
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }

        AdUser user = userRepository.findByUsername(request.getUsername());

        if (user != null) {
            throw new AdException(Constants.ErrorMsg.DUPLICATE_USERNAME_ERROR);
        }

        user = userRepository.save(new AdUser(
                request.getUsername(),
                CommonUtils.sha256(request.getUsername())
        ));

        return new CreateUserResponse(
                user.getId(),
                user.getUsername(),
                user.getToken(),
                user.getCreateTime(),
                user.getUpdateTime()
        );
    }

}
