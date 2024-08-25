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

import java.util.Collection;

@Getter
public class DemoException extends RuntimeException {

    @java.io.Serial
    private static final long serialVersionUID = -3902937129321935L;

    private final ResponseCode code;

    private final Collection<?> data;

    public DemoException(ResponseCode code) {
        super(code.getMessage());
        this.code = code;
        this.data = null;
    }

    public DemoException(ResponseCode code, Collection<?> data) {
        super(code.getMessage());
        this.code = code;
        this.data = data;
    }
}
