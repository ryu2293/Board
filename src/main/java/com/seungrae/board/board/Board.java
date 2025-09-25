package com.seungrae.board.board;

import com.seungrae.board.colum.BoardColumn;
import com.seungrae.board.member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    // Member와 N:M 관계
    @ManyToMany
    @JoinTable(
            name = "board_member",
            joinColumns = @JoinColumn(name = "board_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id")
    )
    private Set<Member> members = new HashSet<>();

    // BoardColumn에 매핑
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("columnOrder ASC")
    List<BoardColumn> columns = new ArrayList<>();
}
