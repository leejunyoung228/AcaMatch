package com.green.acamatch.entity.academy;

import com.green.acamatch.entity.tag.Tag;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@ToString
public class AcademyTag {
    @EmbeddedId
    private AcademyTagIds academyTagIds;

    @ManyToOne
    @MapsId("acaId")
    @JoinColumn(name = "aca_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Academy academy;

    @ManyToOne
    @MapsId("tagId")
    @JoinColumn(name = "tag_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Tag tag;
}
