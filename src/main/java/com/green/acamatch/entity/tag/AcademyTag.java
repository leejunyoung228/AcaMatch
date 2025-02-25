package com.green.acamatch.entity.tag;

import com.green.acamatch.entity.academy.Academy;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Setter
public class AcademyTag {
    @EmbeddedId
    private AcademyTagIds academyTagIds;

    @ManyToOne
    @MapsId("acaId")
    @JoinColumn(name = "aca_id", updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Academy academy;

    @ManyToOne
    @MapsId("tagId")
    @JoinColumn(name = "tag_id", updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Tag Tag;
}
