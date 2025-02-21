package com.green.acamatch.academy.tag;

import com.green.acamatch.entity.tag.Search;
import com.green.acamatch.entity.tag.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchRepository extends JpaRepository<Tag, Long> {
}
