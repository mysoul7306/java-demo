/*
 * Author: rok_root
 * E-mail: mysoul7306@outlook.com
 * Create: 2024. 08. 16
 * Update: 2024. 08. 14
 * All Rights Reserved
 */

package kr.co.rokroot.java.demo.jooq.rest.api.module.order;

import jakarta.annotation.Resource;
import kr.co.rokroot.java.demo.jooq.rest.api.database.mariadb.demo.tables.Order;
import kr.co.rokroot.java.demo.jooq.rest.api.module.order.domain.OrderEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class OrderRepository {

    @Resource(name = "mariaDBDSL")
    private final DSLContext mariaDBDSL;

    private static final Order ORDER = Order.ORDER;

    public void selectOrderDatWithSQLQuery() {
        Record[] rawQuery = mariaDBDSL
                .resultQuery("SELECT * FROM demo.order")
                .fetchArray();
        List.of(rawQuery).forEach(rawResult -> log.info("## RESULT: \n{}", rawResult.intoResultSet()));
    }

    public OrderEntity selectOrderDatWithDSLQuery(Long seq) {
        return mariaDBDSL.selectFrom(ORDER)
                .where(ORDER.SEQ.eq(Math.toIntExact(seq)))
                .fetchOneInto(OrderEntity.class);
    }

}
