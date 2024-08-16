/*
 * Author: rok_root
 * E-mail: mysoul7306@outlook.com
 * Create: 2024. 08. 16
 * Update: 2024. 08. 14
 * All Rights Reserved
 */

package kr.co.rokroot.demo.jooq.rest.api.module.order;

import kr.co.rokroot.demo.core.exceptions.DemoException;
import kr.co.rokroot.demo.jooq.rest.api.module.order.domain.OrderEntity;
import kr.co.rokroot.demo.jooq.rest.api.module.order.domain.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepo;

    public OrderEntity selectOrderData(Long seq) {
        orderRepo.selectOrderDatWithSQLQuery();
        OrderEntity order = orderRepo.selectOrderDatWithDSLQuery(seq);
        if (order == null) {
            throw new DemoException(OrderStatus.FAIL);
        }

        return order;
    }
}
