/*
 * Author: rok_root
 * E-mail: mysoul7306@outlook.com
 * Create: 2024. 08. 16
 * Update: 2024. 08. 14
 * All Rights Reserved
 */

package kr.co.rokroot.java.demo.jooq.rest.api.module.board;

import jakarta.annotation.Resource;
import kr.co.rokroot.java.demo.jooq.rest.api.database.mariadb.demo.tables.Board;
import kr.co.rokroot.java.demo.jooq.rest.api.module.board.domain.BoardEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class BoardRepository {

    @Resource(name = "mariaDBDSL")
    private final DSLContext mariaDBDSL;

    private static final Board BOARD = Board.BOARD;

    public void selectBoardDatWithSQLQuery() {
        Record[] rawQuery = mariaDBDSL
                .resultQuery("SELECT * FROM demo.board")
                .fetchArray();
        List.of(rawQuery).forEach(rawResult -> log.info("## RESULT: \n{}", rawResult.intoResultSet()));
    }

    public BoardEntity selectBoardDatWithDSLQuery(Long seq) {
        return mariaDBDSL.selectFrom(BOARD)
                .where(BOARD.SEQ.eq(Math.toIntExact(seq)))
                .fetchOneInto(BoardEntity.class);
    }

}
