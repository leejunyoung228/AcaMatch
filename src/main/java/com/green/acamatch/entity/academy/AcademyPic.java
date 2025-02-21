package com.green.acamatch.entity.academy;

import com.green.acamatch.entity.datetime.CreatedAt;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Setter
@ToString
public class AcademyPic extends CreatedAt {
    @EmbeddedId
    private AcademyPicIds academyPicIds;

    @ManyToOne
    @MapsId("acaId")
    @JoinColumn(name = "aca_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Academy academy;

}
