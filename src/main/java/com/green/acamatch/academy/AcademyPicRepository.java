package com.green.acamatch.academy;


import com.green.acamatch.entity.academy.AcademyPic;
import com.green.acamatch.entity.academy.AcademyPicIds;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcademyPicRepository extends JpaRepository<AcademyPic, AcademyPicIds> {
}