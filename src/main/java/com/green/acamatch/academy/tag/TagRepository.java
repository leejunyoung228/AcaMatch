package com.green.acamatch.academy.tag;

import com.green.acamatch.entity.tag.Tag;
import org.eclipse.angus.mail.imap.protocol.INTERNALDATE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findByTagNameIn(List<String> tagNames);

}
