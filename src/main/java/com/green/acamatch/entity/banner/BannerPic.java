package com.green.acamatch.entity.banner;

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
public class BannerPic extends CreatedAt {
    @EmbeddedId
    private BannerPicIds bannerPicIds;

    @ManyToOne
    @MapsId("acaId")
    @JoinColumn(name = "aca_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Banner banner;

    @Column(nullable = false)
    private Integer bannerShow;

    @Column(nullable = false)
    private Integer bannerPosition;

}
