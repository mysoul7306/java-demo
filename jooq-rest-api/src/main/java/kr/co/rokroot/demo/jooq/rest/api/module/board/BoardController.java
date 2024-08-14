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
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/rokroot/demo/v1/board")
public class BoardController {

    private final BoardService service;

    @GetMapping(value = "/{seq}")
    public BoardEntity getBoardData(@PathVariable(value = "seq") Long seq) {
        return service.selectBoardData(seq);
    }
}
