/*
 * Author: rok_root
 * E-mail: mysoul7306@outlook.com
 * Create: 2023. 09. 30
 * Update: 2024. 08. 01
 * All Rights Reserved
 */

package kr.co.rokroot.demo.core.codes;

import kr.co.rokroot.demo.core.codes.impl.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum QueryResultCode implements ResponseCode {

    OK("ROK_QRY_0000", "success"),
    FAIL("ROK_QRY_9999", "no data");

    private final String code;
    private final String message;

}
