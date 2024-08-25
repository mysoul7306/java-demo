/*
 * Author: rok_root
 * E-mail: mysoul7306@outlook.com
 * Create: 2023. 09. 30
 * Update: 2024. 08. 16
 * All Rights Reserved
 */

package kr.co.rokroot.java.demo.core.wrappers.res;

import kr.co.rokroot.java.demo.core.abstracts.AbstractResponse;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class RestEmptyResponse extends AbstractResponse implements Serializable {

	public static RestEmptyResponse create() {
		return new RestEmptyResponse();
	}

	public static RestEmptyResponse create(Integer result) {
		return new RestEmptyResponse().resultCnt(result);
	}

	public RestEmptyResponse resultCnt(Integer count) {
		this.count = count;
		if (this.count == null) {
			this.count = 0;
		}

		return this;
	}

	@Override
	public boolean hasData() {
		return this.count > 0;
	}
}