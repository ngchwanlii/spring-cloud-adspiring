package com.adspiring.ad.service;

import com.adspiring.ad.exception.AdException;
import com.adspiring.ad.vo.CreateUserRequest;
import com.adspiring.ad.vo.CreateUserResponse;

public interface IUserService {

    /** create user **/
    CreateUserResponse createUser(CreateUserRequest request) throws AdException;



}
