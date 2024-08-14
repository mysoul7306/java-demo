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
public enum CommonCode implements ResponseCode {

    OK("ROK_CMM_0000", "success"),
    CONNECTION_ERROR("ROK_CMM_5031", "connection error"),
    FAIL("ROK_CMM_9999", "fail");

    private final String code;
    private final String message;

}
