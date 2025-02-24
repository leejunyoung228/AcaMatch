package com.green.acamatch.academy;

import com.green.acamatch.entity.academy.Academy;
import com.green.acamatch.entity.location.Dong;
import com.green.acamatch.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AcademyRepository extends JpaRepository<Academy, Long> {

    List<Academy> findAllByUser(User user);

}
