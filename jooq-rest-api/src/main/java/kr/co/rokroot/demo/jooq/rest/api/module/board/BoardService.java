/*
 * Author: rok_root
 * E-mail: mysoul7306@outlook.com
 * Create: 2023. 09. 30
 * Update: 2024. 08. 13
 * All Rights Reserved
 */

package kr.co.rokroot.demo.jooq.rest.api.module.board;

import kr.co.rokroot.demo.jooq.rest.api.module.board.domain.BoardEntity;
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

        return board;
    }
}
