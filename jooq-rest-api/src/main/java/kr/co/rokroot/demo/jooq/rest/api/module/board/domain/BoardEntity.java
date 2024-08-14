/*
 * Author: rok_root
 * E-mail: mysoul7306@outlook.com
 * Create: 2023. 09. 30
 * Update: 2024. 08. 13
 * All Rights Reserved
 */

package kr.co.rokroot.demo.jooq.rest.api.module.board.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "board")
public class BoardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq", precision = 13, nullable = false)
    private Long seq;

    @Column(name = "code")
    private String code;

    @Column(name = "writer")
    private String writer;

    @Column(name = "title")
    private String title;

    @Column(name = "text")
    private String text;

    @Column(name = "del")
    private String del;

    @Column(name = "create_time")
    private String createTime;

    @Column(name = "update_time")
    private String updateTime;

}
