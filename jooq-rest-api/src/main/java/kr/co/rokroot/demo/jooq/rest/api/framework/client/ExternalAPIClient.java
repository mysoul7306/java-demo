/*
 * Author: rok_root
 * E-mail: mysoul7306@outlook.com
 * Create: 2023. 09. 30
 * Update: 2024. 08. 16
 * All Rights Reserved
 */

package kr.co.rokroot.demo.jooq.rest.api.framework.client;

import kr.co.rokroot.demo.core.codes.CommonCode;
import kr.co.rokroot.demo.core.exceptions.ExternalAPIClientException;
import kr.co.rokroot.demo.core.types.HostType;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExternalAPIClient {

    @Resource(mappedName = "externalAPIClient")
    private WebClient client;

    public JSONObject callApi(HostType host, RequestMethod method, String uri) throws ExternalAPIClientException {
        return this.callApi(host, method, uri, new JSONObject());
    }

    public JSONObject callApi(HostType host, RequestMethod method, String uri, JSONObject json) throws ExternalAPIClientException {
        try {
            return client.method(method.asHttpMethod())
                    .uri(uri)
                    .body(Mono.justOrEmpty(json), JSONObject.class)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, res -> {
                        switch (HttpStatus.valueOf(res.statusCode().value())) {
                            case UNAUTHORIZED, NOT_FOUND -> {
                                return Mono.error(new ResponseStatusException(res.statusCode()));
                            }
                            case FORBIDDEN -> {
                                log.error("### {} API Forbidden: {}", host.getType(), uri);
                                return Mono.empty();
                            }
                            default -> {
                                log.error("### {} API call failed: {}", host.getType(), res.statusCode());
                                return Mono.empty();
                            }
                        }
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, res -> {
                        switch (HttpStatus.valueOf(res.statusCode().value())) {
                            case INTERNAL_SERVER_ERROR, NOT_IMPLEMENTED -> {
                                log.error("### {} API call failed: {}", host.getType(), res.statusCode());
                                return Mono.empty();
                            }
                            default -> {
                                return Mono.error(new ResponseStatusException(res.statusCode()));
                            }
                        }
                    })
                    .bodyToMono(JSONObject.class)
                    .defaultIfEmpty(new JSONObject())
                    .doOnError(err -> {
                        throw new ExternalAPIClientException(CommonCode.CONNECTION_ERROR);
                    })
                    .block();
        } catch (WebClientRequestException wcre) {
            log.error("### Web client request error occurred: {}", wcre.getMessage());

            return new JSONObject();
        } catch (WebClientResponseException wcre) {
            log.error("### Web client response error occurred: {}", wcre.getMessage());

            return new JSONObject();
        }
    }
}
