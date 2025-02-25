package com.green.acamatch.acaClass;

import com.green.acamatch.entity.category.ClassCategory;
import com.green.acamatch.entity.category.ClassCategoryIds;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassCategoryRepository extends JpaRepository<ClassCategory, ClassCategoryIds> {

    @Query("SELECT COUNT(*) FROM ClassCategory a WHERE a.classId.classId = :classId AND a.classCategoryIds.categoryId = :categoryId")
    Long existsCategory(@Param("classId") Long classId, @Param("categoryId") Long categoryId );
}