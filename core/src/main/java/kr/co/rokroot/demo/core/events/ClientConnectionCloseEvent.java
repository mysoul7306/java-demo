/*
 * Author: rok_root
 * E-mail: mysoul7306@outlook.com
 * Create: 2023. 09. 30
 * Update: 2024. 08. 01
 * All Rights Reserved
 */

package kr.co.rokroot.demo.core.events;

import org.springframework.context.ApplicationEvent;

public class ClientConnectionCloseEvent extends ApplicationEvent {

    @java.io.Serial
    private static final long serialVersionUID = 3092095391002390920L;

    public ClientConnectionCloseEvent(Object source) {
        super(source);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
