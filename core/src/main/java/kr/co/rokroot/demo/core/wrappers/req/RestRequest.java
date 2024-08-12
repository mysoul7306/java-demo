/*
 * Author: rok_root
 * E-mail: mysoul7306@outlook.com
 * Create: 2023. 09. 30
 * Update: 2024. 08. 01
 * All Rights Reserved
 */

package kr.co.rokroot.demo.core.wrappers.req;

import kr.co.rokroot.demo.core.abstracts.AbstractRestRequest;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class RestRequest<T extends Serializable> extends AbstractRestRequest implements Serializable {

	public static <T extends Serializable> RestRequest<T> create() {
		return new RestRequest<>();
	}

	public static <T extends Serializable> RestRequest<T> create(T clazz) {
		return new RestRequest<T>().add(clazz);
	}

	private T data;

	private RestRequest<T> add(T clazz) {
		if (clazz == null) {
			return this;
		}
		this.data = clazz;
		return this;
	}

	@Override
	public boolean hasData() {
		return this.data != null;
	}
}