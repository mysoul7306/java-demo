/*
 * Author: rok_root
 * E-mail: mysoul7306@outlook.com
 * Create: 2024. 08. 16
 * Update: 2024. 08. 14
 * All Rights Reserved
 */

package kr.co.rokroot.java.demo.jooq.rest.api.module.board;

import kr.co.rokroot.java.demo.core.exceptions.DemoException;
import kr.co.rokroot.java.demo.jooq.rest.api.module.board.domain.BoardEntity;
import kr.co.rokroot.java.demo.jooq.rest.api.module.board.domain.BoardStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepo;

    public BoardEntity selectBoardData(Long seq) {
        boardRepo.selectBoardDatWithSQLQuery();
        BoardEntity board = boardRepo.selectBoardDatWithDSLQuery(seq);
        if (board == null) {
            throw new DemoException(BoardStatus.FAIL);
        }

        return board;
    }
}
