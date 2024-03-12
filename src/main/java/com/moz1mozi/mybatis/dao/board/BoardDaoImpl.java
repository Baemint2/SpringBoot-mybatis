package com.moz1mozi.mybatis.dao.board;

import com.moz1mozi.mybatis.dto.BoardDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
@RequiredArgsConstructor
@Service
public class BoardDaoImpl {


    private final BoardDao boardDao;

    public int insertBoard(BoardDto board) {
        return boardDao.insertBoard(board);
    }

    public int updateBoard(BoardDto board) {
        return boardDao.updateBoard(board);
    }

    public int deleteBoard(int board_no) {
        return boardDao.deleteBoard(board_no);
    }

    public int getBoardCount() {
        return boardDao.getBoardCount();
    }

    public BoardDto selectBoardByNo(int board_no) {
        return boardDao.selectBoardByNo(board_no);
    }
}
