/*
 * Author: rok_root
 * E-mail: mysoul7306@outlook.com
 * Create: 2024. 08. 16
 * Update: 2024. 08. 14
 * All Rights Reserved
 */

package kr.co.rokroot.demo.jooq.rest.api.module.board;

import kr.co.rokroot.demo.jooq.rest.api.framework.client.ExternalAPIClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {

    private BoardService service;
    private ExternalAPIClient apiClient;

    @BeforeEach
    public void init() {
        this.apiClient = Mockito.mock(ExternalAPIClient.class);
//        this.service = new DailyService(this.apiClient, this.templateEngine);
    }
}
