package com.moz1mozi.mybatis.dao;

import com.moz1mozi.mybatis.dto.BoardDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BoardDao {
    int insertBoard(BoardDto board);
    int updateBoard(BoardDto board);
    int deleteBoard(int board_no);
    int getBoardCount();
    BoardDto selectBoardByNo(int board_no);
}
