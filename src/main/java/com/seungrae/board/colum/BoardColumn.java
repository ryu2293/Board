package com.seungrae.board.colum;

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
    private Board board;

    @OneToMany(mappedBy = "boardColumn", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("cardOrder ASC")
    private List<Card> cards = new ArrayList<>();
}
