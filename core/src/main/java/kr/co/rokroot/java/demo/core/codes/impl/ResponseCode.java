/*
 * Author: rok_root
 * E-mail: mysoul7306@outlook.com
 * Create: 2023. 09. 30
 * Update: 2024. 08. 01
 * All Rights Reserved
 */

package kr.co.rokroot.java.demo.core.codes.impl;

import kr.co.rokroot.java.demo.core.codes.CommonCode;

public interface ResponseCode {

    ResponseCode OK = CommonCode.OK;
    ResponseCode FAIL = CommonCode.FAIL;

    String getCode();
    String getMessage();

}
