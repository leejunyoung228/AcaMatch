package com.green.acamatch.popUp;

import com.green.acamatch.entity.popUp.PopUp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PopUpRepository extends JpaRepository<PopUp, Long> {
}