package com.sparta.mys3study.practice.repository;

import com.sparta.mys3study.practice.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
