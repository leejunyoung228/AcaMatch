package com.green.acamatch.entity.tag;

import com.green.acamatch.entity.datetime.CreatedAt;
import com.green.acamatch.entity.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Search extends CreatedAt {
    @Id
    private Long searchId;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tag Tag;
}
