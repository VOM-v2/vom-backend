package okodee.vom.domain.profile.repository;

import java.util.List;
import java.util.UUID;
import okodee.vom.domain.profile.entity.Keyword;
import okodee.vom.domain.profile.entity.UserKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserKeywordRepository extends JpaRepository<UserKeyword, UUID> {
    /**
     * 특정 사용자의 모든 키워드 조회 (Keyword 정보 함께 fetch)
     */
    @Query("SELECT uk FROM UserKeyword uk " +
        "JOIN FETCH uk.keyword k " +
        "WHERE uk.userId = :userId " +
        "ORDER BY k.category ASC, k.displayOrder ASC")
    List<UserKeyword> findAllByUserIdWithKeyword(@Param("userId") UUID userId);

    /**
     * 사용자의 키워드 개수 조회
     */
    long countByUserId(UUID userId);

    /**
     * 특정 사용자가 특정 키워드를 가지고 있는지 확인
     */
    boolean existsByUserIdAndKeyword(UUID userId, Keyword keyword);

    /**
     * 사용자의 모든 키워드 삭제
     */
    void deleteAllByUserId(UUID userId);
}
