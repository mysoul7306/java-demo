/*
 * Author: rok_root
 * E-mail: mysoul7306@outlook.com
 * Create: 2023. 09. 30
 * Update: 2024. 08. 13
 * All Rights Reserved
 */

package kr.co.rokroot.demo.jooq.rest.api.module.order.domain;

import kr.co.rokroot.demo.core.codes.impl.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatus implements ResponseCode {

    OK("RPT_ODR_0000", "success"),
    FAIL("RPT_ODR_9999", "fail");

    private final String code;
    private final String message;

}
