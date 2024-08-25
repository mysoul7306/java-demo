/*
 * Author: rok_root
 * E-mail: mysoul7306@outlook.com
 * Create: 2023. 09. 30
 * Update: 2024. 08. 10
 * All Rights Reserved
 */

package kr.co.rokroot.java.demo.core.wrappers.req;

import kr.co.rokroot.java.demo.core.abstracts.AbstractRequest;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.kafka.common.header.Headers;
import org.json.simple.JSONObject;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class KafkaRequest extends AbstractRequest implements Serializable {

    private Headers headers;
    private JSONObject data;

    @Override
    public boolean hasData() {
        return this.data != null;
    }
}
