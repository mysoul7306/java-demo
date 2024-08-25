/*
 * Author: rok_root
 * E-mail: mysoul7306@outlook.com
 * Create: 2023. 09. 30
 * Update: 2024. 08. 16
 * All Rights Reserved
 */

package kr.co.rokroot.java.demo.core.abstracts;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public abstract class AbstractRequest {

	private LocalDateTime reqTime;

	public abstract boolean hasData();
}