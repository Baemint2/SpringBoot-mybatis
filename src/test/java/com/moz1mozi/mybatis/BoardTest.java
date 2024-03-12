package com.moz1mozi.mybatis;

import com.moz1mozi.mybatis.dao.BoardDaoImpl;
import com.moz1mozi.mybatis.dto.BoardDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class BoardTest {

    @Autowired
    private BoardDaoImpl boardDaoImpl;

    @Test
    void 등록테스트() {
        BoardDto board = BoardDto.builder()
                .title("테스트제목")
                .contents("테스트내용")
                .build();
        int result = boardDaoImpl.insertBoard(board);
        assertEquals(1, result);
    }

    @Test
    void 수정테스트() {
        BoardDto board = BoardDto.builder()
                .board_no(1)
                .title("[수정]테스트 제목")
                .contents("[수정]테스트 내용")
                .build();

        int updateCount = boardDaoImpl.updateBoard(board);
        assertEquals(1, updateCount);
    }

    @Test
    void 삭제테스트() {
        int deleteCount = boardDaoImpl.deleteBoard(1);
        assertEquals(1, deleteCount);
    }

    @Test
    void 상세테스트() {
        BoardDto board = boardDaoImpl.selectBoardByNo(2);

        assertNotNull(board);
    }

    @Test
    void 목록개수테스트() {
        int boardCount = boardDaoImpl.getBoardCount();
        assertEquals(3, boardCount);
    }
}
