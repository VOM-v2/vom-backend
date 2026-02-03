package okodee.vom.domain.profile.repository;

import java.util.List;
import okodee.vom.domain.profile.entity.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KeywordRepository extends JpaRepository<Keyword, Long> {
    /**
     * 카테고리별 키워드 조회 (정렬 순서대로)
     */
    List<Keyword> findByCategoryOrderByDisplayOrder(Keyword.KeywordCategory category);

    /**
     * 전체 키워드 조회 (카테고리별 정렬 순서대로)
     */
    List<Keyword> findAllByOrderByCategoryAscDisplayOrderAsc();
}
