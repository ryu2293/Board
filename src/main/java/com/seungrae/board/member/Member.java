package com.seungrae.board.member;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.seungrae.board.board.Board;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String username;
    private String password;
    private String role;
    @ManyToMany(mappedBy = "members")
    @JsonBackReference
    private Set<Board> boards = new HashSet<>();

}
