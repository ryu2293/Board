package com.seungrae.board.card;

import com.seungrae.board.colum.BoardColumn;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private Integer cardOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boardColumn_id")
    private BoardColumn boardColumn;
}
