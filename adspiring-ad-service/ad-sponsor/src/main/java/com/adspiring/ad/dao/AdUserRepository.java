package com.adspiring.ad.dao;

import com.adspiring.ad.entity.AdUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdUserRepository extends JpaRepository<AdUser, Long> {

    AdUser findByUsername(String username);

}
