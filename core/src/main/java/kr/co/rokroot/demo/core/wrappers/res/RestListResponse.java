/*
 * Author: rok_root
 * E-mail: mysoul7306@outlook.com
 * Create: 2023. 09. 30
 * Update: 2024. 08. 01
 * All Rights Reserved
 */

package kr.co.rokroot.demo.core.wrappers.res;

import kr.co.rokroot.demo.core.abstracts.AbstractRestResponse;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class RestListResponse<T extends Serializable> extends AbstractRestResponse implements Serializable {

	public static <T extends Serializable> RestListResponse<T> create(T clazz) {
		return new RestListResponse<T>().add(clazz);
	}

	@SafeVarargs
	public static <T extends Serializable> RestListResponse<T> create(T... clazz) {
		return new RestListResponse<T>().add(clazz);
	}

	public static <T extends Serializable> RestListResponse<T> create(List<T> clazz) {
		return new RestListResponse<T>().add(clazz);
	}

	public RestListResponse<T> resultCnt(Integer count) {
		this.count = count;
		if (this.count == null) {
			this.count = 0;
		}
		return this;
	}

	private List<T> data = new ArrayList<>();

	private RestListResponse<T> add(T clazz) {
		if (clazz == null) {
			return this;
		}
		this.data.add(clazz);
		return this;
	}

	@SafeVarargs
	private RestListResponse<T> add(T... ts) {
		if (ts == null) {
			return this;
		}
		for (T t : ts) {
			add(t);
		}
		return this;
	}

	private RestListResponse<T> add(List<T> list) {
		if (list == null) {
			return this;
		}
		this.data.addAll(list);
		return this;
	}

	@Override
	public boolean hasData() {
		return this.data != null && !this.data.isEmpty();
	}
}