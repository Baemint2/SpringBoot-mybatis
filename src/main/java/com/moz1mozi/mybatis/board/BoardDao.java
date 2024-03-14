package com.moz1mozi.mybatis.board;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BoardDao {
    int insertBoard(BoardDto board);
    int updateBoard(BoardDto board);
    int deleteBoard(int board_no);
    int getBoardCount();
    BoardDto selectBoardByNo(int board_no);
}
