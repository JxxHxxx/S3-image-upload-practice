package com.sparta.mys3study.practice.service;

import com.sparta.mys3study.practice.entity.Board;
import com.sparta.mys3study.practice.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public Board createBoard(String imageURL, String title) {
        Board board = new Board(title, imageURL);

        return boardRepository.save(board);
    }

    public Board findBoard(Long boardsId) {
        Board findBoard = boardRepository.findById(boardsId).orElseThrow();

        return findBoard;
    }

    public void deleteBoard(Long boardId) {
        boardRepository.deleteById(boardId);
    }
}
