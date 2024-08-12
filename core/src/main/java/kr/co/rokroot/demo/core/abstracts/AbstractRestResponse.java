/*
 * Author: rok_root
 * E-mail: mysoul7306@outlook.com
 * Create: 2023. 09. 30
 * Update: 2024. 08. 01
 * All Rights Reserved
 */

package kr.co.rokroot.demo.core.abstracts;

import kr.co.rokroot.demo.core.codes.QueryResultCode;
import kr.co.rokroot.demo.core.codes.impl.ResponseCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public abstract class AbstractRestResponse {

	private ResponseCode resultType = QueryResultCode.OK;
	private LocalDateTime resTime = LocalDateTime.now();
	protected Integer count;

	public abstract boolean hasData();
}