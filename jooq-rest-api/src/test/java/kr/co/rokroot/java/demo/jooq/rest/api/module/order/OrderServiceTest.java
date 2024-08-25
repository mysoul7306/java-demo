/*
 * Author: rok_root
 * E-mail: mysoul7306@outlook.com
 * Create: 2024. 08. 16
 * Update: 2024. 08. 14
 * All Rights Reserved
 */

package kr.co.rokroot.java.demo.jooq.rest.api.module.order;

import kr.co.rokroot.java.demo.jooq.rest.api.framework.client.ExternalAPIClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    private OrderService service;
    private ExternalAPIClient apiClient;

    @BeforeEach
    public void init() {
        this.apiClient = Mockito.mock(ExternalAPIClient.class);
//        this.service = new MonthlyService(this.apiClient, this.templateEngine);
    }
}
