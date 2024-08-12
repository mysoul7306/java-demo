/*
 * Author: rok_root
 * E-mail: mysoul7306@outlook.com
 * Create: 2023. 09. 30
 * Update: 2024. 08. 12
 * All Rights Reserved
 */

package kr.co.rokroot.demo.jooq.rest.api.framework.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.rokroot.demo.core.codes.impl.ResponseCode;
import kr.co.rokroot.demo.core.exceptions.DemoException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.rokroot.demo.core.wrappers.res.RestSingleResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.concurrent.RejectedExecutionException;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionAspect {

    private final ObjectMapper om;

    @ExceptionHandler(BindException.class)
    @ResponseBody
    private ResponseEntity<JSONObject> interceptBindException(HttpServletRequest req, HttpServletResponse res, BindException e) {
        return this.responseException(req, HttpStatus.BAD_REQUEST, ResponseCode.FAIL, e);
    }

    @ExceptionHandler(WebClientRequestException.class)
    @ResponseBody
    private ResponseEntity<JSONObject> interceptWebClientRequestException(HttpServletRequest req, HttpServletResponse res,
                                                                          WebClientRequestException e) {
        return this.responseException(req, HttpStatus.SERVICE_UNAVAILABLE, ResponseCode.FAIL, e);
    }

    @ExceptionHandler(ResponseStatusException.class)
    @ResponseBody
    private ResponseEntity<JSONObject> interceptResponseStatusException(HttpServletRequest req, HttpServletResponse res, ResponseStatusException e) {
        return this.responseException(req, e.getStatusCode(), ResponseCode.FAIL, e);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    @ResponseBody
    private ResponseEntity<JSONObject> interceptHttpClientErrorException(HttpServletRequest req, HttpServletResponse res,
                                                                         HttpClientErrorException e) {
        return this.responseException(req, e.getStatusCode(), ResponseCode.FAIL, e);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    private ResponseEntity<JSONObject> interceptHttpRequestMethodNotSupportedException(HttpServletRequest req, HttpServletResponse res,
                                                                                       HttpRequestMethodNotSupportedException e) {
        return this.responseException(req, HttpStatus.BAD_REQUEST, ResponseCode.FAIL, e);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    private ResponseEntity<JSONObject> interceptIllegalArgumentException(HttpServletRequest req, HttpServletResponse res,
                                                                         IllegalArgumentException e) {
        return this.responseException(req, HttpStatus.BAD_REQUEST, ResponseCode.FAIL, e);
    }

    @ExceptionHandler(DemoException.class)
    @ResponseBody
    private ResponseEntity<JSONObject> interceptIllegalRuleArgumentException(HttpServletRequest req, HttpServletResponse res,
                                                                             DemoException e) {
        return this.responseException(req, HttpStatus.BAD_REQUEST, e.getCode(), e);
    }

    @ExceptionHandler(RejectedExecutionException.class)
    @ResponseBody
    private ResponseEntity<JSONObject> interceptRejectedExecutionException(HttpServletRequest req, HttpServletResponse res,
                                                                           RejectedExecutionException e) {
        return this.responseException(req, HttpStatus.INTERNAL_SERVER_ERROR, ResponseCode.FAIL, e);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    private ResponseEntity<JSONObject> interceptException(HttpServletRequest req, HttpServletResponse res, Exception e) {
        return this.responseException(req, HttpStatus.INTERNAL_SERVER_ERROR, ResponseCode.FAIL, e);
    }

    private ResponseEntity<JSONObject> responseException(HttpServletRequest req, HttpStatusCode status, ResponseCode code, Exception e) {
        return new ResponseEntity<JSONObject>(om.convertValue(this.loggingException(req, code, e), JSONObject.class), status);
    }

    private RestSingleResponse<JSONObject> loggingException(HttpServletRequest req, ResponseCode code, Exception e) {
        log.error("=============== EXCEPTION ===============");
        log.error("Request URI: {}", req.getRequestURI());
        log.error("Exception name: {}", e.getClass().getSimpleName());
        log.error("Exception message: {}", e.getMessage());

        Enumeration<?> en = req.getParameterNames();
        if (en.hasMoreElements()) {
            String param;
            String[] values;

            log.error("=============== PARAMETER ===============");
            while (en.hasMoreElements()) {
                param = (String) en.nextElement();
                values = req.getParameterValues(param);

                if (values.length == 1) {
                    log.error(param + ": " + values[0]);
                } else {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < values.length; i++) {
                        sb.append(values[i]);
                        if (i != values.length - 1) {
                            sb.append(",");
                        }
                    }
                    log.error(param + ": " + sb);
                }
            }
        }

        log.error("=========================================");

        if (log.isDebugEnabled()) {
            e.printStackTrace();
        }

        JSONObject result = new JSONObject();
        result.put("path", req.getRequestURI());
        result.put("code", code);

        if (e instanceof BindException) {
            result.put("message", this.loggingValidateErrors(((BindException) e).getFieldErrors()));
        } else {
            result.put("message", e.getMessage());
        }

        return RestSingleResponse.create(result);
    }

    private String loggingValidateErrors(Collection<FieldError> errors) {
        StringBuilder msg = new StringBuilder();
        Iterator<FieldError> it = errors.stream().sorted(Comparator.comparing(FieldError::getField)).iterator();
        while (it.hasNext()) {
            FieldError error = it.next();
            msg.append(error.getField());
            msg.append(": ");
            msg.append(error.getDefaultMessage());

            if (it.hasNext()) {
                msg.append(", ");
            }
        }

        return msg.toString();
    }
}
