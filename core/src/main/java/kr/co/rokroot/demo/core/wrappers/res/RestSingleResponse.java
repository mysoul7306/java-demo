/*
 * Author: rok_root
 * E-mail: mysoul7306@outlook.com
 * Create: 2023. 09. 30
 * Update: 2024. 08. 16
 * All Rights Reserved
 */

package kr.co.rokroot.demo.core.wrappers.res;

import kr.co.rokroot.demo.core.abstracts.AbstractResponse;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class RestSingleResponse<T extends Serializable> extends AbstractResponse implements Serializable {

	public static <T extends Serializable> RestSingleResponse<T> create() {
		return new RestSingleResponse<>();
	}

	public static <T extends Serializable> RestSingleResponse<T> create(T clazz) {
		return new RestSingleResponse<T>().add(clazz);
	}

	public RestSingleResponse<T> resultCnt(Integer count) {
		this.count = count;
		if (this.count == null) {
			this.count = 0;
		}
		return this;
	}

	private T data;

	private RestSingleResponse<T> add(T clazz) {
		if (clazz == null) {
			return this;
		}
		this.data = clazz;
		return this;
	}

	@Override
	public boolean hasData() {
		return data != null;
	}
}