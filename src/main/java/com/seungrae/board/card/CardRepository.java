package com.seungrae.board.card;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CardRepository extends JpaRepository<Card, Long> {
    // 특정 컬럼에서 주어진 순서보다 큰 카드들의 순서를 1씩 증가
    @Modifying(clearAutomatically = true) // 업데이트 후 캐시를 자동으로 비움
    @Query("UPDATE Card c SET c.cardOrder = c.cardOrder + 1 WHERE c.boardColumn.id = :columnId AND c.cardOrder >= :order")
    void incrementOrderAfter(@Param("columnId") Long columnId, @Param("order") Integer order);
    // 특정 컬럼에서 주어진 순서보다 큰 카드들의 순서를 1씩 감소
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Card c SET c.cardOrder = c.cardOrder - 1 WHERE c.boardColumn.id = :columnId AND c.cardOrder > :order")
    void decrementOrderAfter(@Param("columnId") Long columnId, @Param("order") Integer order);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Card c SET c.cardOrder = c.cardOrder + 1 WHERE c.boardColumn.id = :columnId AND c.cardOrder >= :startOrder AND c.cardOrder < :endOrder")
    void incrementOrderBetween(
            @Param("columnId") Long columnId,
            @Param("startOrder") Integer startOrder,
            @Param("endOrder") Integer endOrder
    );

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Card c SET c.cardOrder = c.cardOrder - 1 WHERE c.boardColumn.id = :columnId AND c.cardOrder > :startOrder AND c.cardOrder <= :endOrder")
    void decrementOrderBetween(
            @Param("columnId") Long columnId,
            @Param("startOrder") Integer startOrder,
            @Param("endOrder") Integer endOrder
    );
}
