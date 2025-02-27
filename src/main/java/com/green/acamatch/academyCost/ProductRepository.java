package com.green.acamatch.academyCost;

import com.green.acamatch.entity.acaClass.AcaClass;
import com.green.acamatch.entity.academyCost.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByClassId(AcaClass classId);
}
