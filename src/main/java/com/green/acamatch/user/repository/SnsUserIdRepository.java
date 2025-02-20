package com.green.acamatch.user.repository;

import com.green.acamatch.entity.myenum.SignInProviderType;
import com.green.acamatch.entity.user.SnsUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SnsUserIdRepository extends JpaRepository<SnsUser,String> {
    SnsUser findBySnsUserIdAndId_SignInProviderType(String snsUserId, SignInProviderType idSignInProviderType);
}
