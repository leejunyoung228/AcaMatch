package com.green.acamatch.entity.banner;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Embeddable
@EqualsAndHashCode
public class BannerPicIds implements Serializable {

    private Long acaId;

    @Column(length = 50)
    private String bannerPic;
}
