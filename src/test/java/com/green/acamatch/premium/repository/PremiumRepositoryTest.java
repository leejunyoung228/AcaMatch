package com.green.acamatch.premium.repository;

import com.green.acamatch.academy.PremiumRepository;
import com.green.acamatch.config.JpaAuditingConfiguration;
import com.green.acamatch.entity.academy.Academy;
import com.green.acamatch.entity.academy.PremiumAcademy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest //JPA 관련 테스트를 수행할 때 사용하는 어노테이션
@Import(JpaAuditingConfiguration.class) //created_at, updated_at 현재 일시값 들어갈 수 있도록 auditing 기능 활성화
@ActiveProfiles("test") //어떤 yaml 쓸건지, 테스트용 설정 파일(application-test.yml) 사용
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PremiumRepositoryTest {

    @Autowired
    PremiumRepository premiumRepository;

    @Autowired
     TestEntityManager entityManager;

     Academy academy;
     PremiumAcademy premiumAcademy;

    @BeforeEach
    void setUp() {
        // Academy 엔티티 생성
        academy = new Academy();
        academy.setAcaId(1L);
        academy.setAcaName("Test Academy");
        academy.setPremium(0);
        entityManager.persist(academy);

        // PremiumAcademy 엔티티 생성
        premiumAcademy = new PremiumAcademy();
        premiumAcademy.setAcaId(academy.getAcaId()); // acaId 설정
        premiumAcademy.setAcademy(academy); // Academy 설정
        premiumAcademy.setPreCheck(0);
        premiumAcademy.setPrice(100000);
        entityManager.persistAndFlush(premiumAcademy);
    }

    @Test
    @DisplayName("학원 ID로 PremiumAcademy 조회")
    void testFindByAcademy_AcaId() {
        // when
        Optional<PremiumAcademy> result = premiumRepository.findByAcademy_AcaId(academy.getAcaId());

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getAcaId()).isEqualTo(academy.getAcaId());
        assertThat(result.get().getAcademy().getAcaName()).isEqualTo("Test Academy");
    }

    @Test
    @DisplayName("존재하지 않는 학원 ID로 조회 시 빈 값 반환")
    void testFindByAcademy_AcaId_NotFound() {
        // when
        Optional<PremiumAcademy> result = premiumRepository.findByAcademy_AcaId(999L); // 존재하지 않는 ID

        // then
        assertThat(result).isEmpty();
    }
}