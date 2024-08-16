/*
 * Author: rok_root
 * E-mail: mysoul7306@outlook.com
 * Create: 2024. 08. 16
 * Update: 2024. 08. 16
 * All Rights Reserved
 */

package kr.co.rokroot.demo.jooq.rest.api.module.order;

import kr.co.rokroot.demo.jooq.rest.api.module.order.domain.OrderEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/rokroot/demo/v1/order")
public class OrderController {

    private final OrderService service;

    @GetMapping(value = "/{seq}")
    public OrderEntity getOrderData(@PathVariable(value = "seq") Long seq) {
        return service.selectOrderData(seq);
    }
}
