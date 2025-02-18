package com.green.acamatch.user.repository;

import com.green.acamatch.entity.myenum.SignInProviderType;
import com.green.acamatch.entity.user.SnsUserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SnsUserIdRepository extends JpaRepository<SnsUserId,String> {
    SnsUserId findBySnsUserIdAndSignInProviderType(String snsUserId, SignInProviderType signInProviderType);
}
