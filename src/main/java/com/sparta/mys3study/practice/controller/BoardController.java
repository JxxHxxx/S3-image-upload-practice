package com.sparta.mys3study.practice.controller;

import com.sparta.mys3study.practice.entity.Board;
import com.sparta.mys3study.practice.service.AmazonS3Service;
import com.sparta.mys3study.practice.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;


@Slf4j
@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final AmazonS3Service amazonS3Service;

    @Value("${cloud.aws.s3.image.dns}")
    private String s3ImageOrigin;


    @PostMapping("/v2/boards")
    public ResponseEntity<Object> createBoardV2(@RequestParam("images") MultipartFile multipartFile,
                                                @RequestParam String title) throws IOException {


        String objectKey = amazonS3Service.putS3Object(multipartFile); // S3에 이미지 파일 저장

        String imageURL = s3ImageOrigin + objectKey;
        boardService.createBoard(imageURL, title); // DB에 이미지 URL 저장

        return new ResponseEntity<>("저장 완료", HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping("/v2/boards/{boardsId}")
    public ResponseEntity<Board> findBoardV2(@PathVariable Long boardsId) {
        Board board = boardService.findBoard(boardsId);
        return new ResponseEntity<>(board,HttpStatus.OK);
    }
}
