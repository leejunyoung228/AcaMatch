package com.green.acamatch.academy.tag;

import com.green.acamatch.entity.tag.AcademyTag;
import com.green.acamatch.entity.tag.AcademyTagIds;
import com.green.acamatch.entity.tag.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AcademyTagRepository extends JpaRepository<AcademyTag, AcademyTagIds> {

    @Modifying
    @Query(value = "delete from AcademyTag a where a.academy.acaId=:acaId")
    int deleteAcademytag(Long acaId);
}
