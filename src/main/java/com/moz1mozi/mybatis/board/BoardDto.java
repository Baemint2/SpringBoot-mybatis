package com.moz1mozi.mybatis.board;


import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class BoardDto {
    private int board_no;
    private String title;
    private String contents;
    private Date modified_et;
    private Date created_at;
}
