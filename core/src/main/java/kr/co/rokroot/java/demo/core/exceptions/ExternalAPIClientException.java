/*
 * Author: rok_root
 * E-mail: mysoul7306@outlook.com
 * Create: 2023. 09. 30
 * Update: 2024. 08. 02
 * All Rights Reserved
 */

package kr.co.rokroot.java.demo.core.exceptions;

import kr.co.rokroot.java.demo.core.codes.impl.ResponseCode;
import lombok.Getter;

@Getter
public class ExternalAPIClientException extends RuntimeException {

    private final ResponseCode code;

    public ExternalAPIClientException(ResponseCode code) {
        super(code.getMessage());
        this.code = code;
    }
}
