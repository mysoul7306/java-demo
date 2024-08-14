/*
 * Author: rok_root
 * E-mail: mysoul7306@outlook.com
 * Create: 2023. 09. 30
 * Update: 2024. 08. 13
 * All Rights Reserved
 */

package kr.co.rokroot.demo.jooq.rest.api.module.order;

import kr.co.rokroot.demo.jooq.rest.api.framework.client.RestApiClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    private OrderService service;
    private RestApiClient apiClient;

    @BeforeEach
    public void init() {
        this.apiClient = Mockito.mock(RestApiClient.class);
//        this.service = new MonthlyService(this.apiClient, this.templateEngine);
    }
}
