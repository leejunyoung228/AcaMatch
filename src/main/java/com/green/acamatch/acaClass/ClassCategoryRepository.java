package com.green.acamatch.acaClass;

import com.green.acamatch.entity.category.ClassCategory;
import com.green.acamatch.entity.category.ClassCategoryIds;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassCategoryRepository extends JpaRepository<ClassCategory, ClassCategoryIds> {

    @Query("SELECT COUNT(*) FROM ClassCategory a WHERE a.classId = :classId AND a.classCategoryIds.categoryId = :categoryId")
    Long existsCategory(Long classId, Long categoryId );
}