package com.green.acamatch.like;

import com.green.acamatch.entity.like.Like;
import com.green.acamatch.entity.like.LikeIds;
import com.green.acamatch.entity.popUp.PopUp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, LikeIds> {
}
