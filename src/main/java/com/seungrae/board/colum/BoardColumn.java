package com.seungrae.board.colum;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.seungrae.board.board.Board;
import com.seungrae.board.card.Card;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.annotation.Order;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class BoardColumn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Integer columnOrder;
    // FK(Board_id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    @JsonBackReference // 양방향 연관관계 자식. json 변환시 무한루프 방지
    private Board board;

    @OneToMany(mappedBy = "boardColumn", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("cardOrder ASC")
    @JsonManagedReference // 양방향 연관관계 부모. json 변환시 무한루프 방지
    private List<Card> cards = new ArrayList<>();
}
