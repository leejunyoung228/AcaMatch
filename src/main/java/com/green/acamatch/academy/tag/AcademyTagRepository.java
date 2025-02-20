package com.green.acamatch.academy.tag;

import com.green.acamatch.entity.tag.AcademyTag;
import com.green.acamatch.entity.tag.AcademyTagIds;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcademyTagRepository extends JpaRepository<AcademyTag, AcademyTagIds> {
}
