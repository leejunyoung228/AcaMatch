package com.green.acamatch.academy;


import com.green.acamatch.entity.academy.AcademyPic;
import com.green.acamatch.entity.academy.AcademyPicIds;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AcademyPicRepository extends JpaRepository<AcademyPic, AcademyPicIds> {

    @Modifying
    @Query(" delete from AcademyPic a where a.academy.acaId=:acaId")
    int deleteAcademyPicsByAcaId(Long acaId);
}