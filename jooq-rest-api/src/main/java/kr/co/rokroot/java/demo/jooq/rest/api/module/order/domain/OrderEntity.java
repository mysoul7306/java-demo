/*
 * Author: rok_root
 * E-mail: mysoul7306@outlook.com
 * Create: 2024. 08. 16
 * Update: 2024. 08. 13
 * All Rights Reserved
 */

package kr.co.rokroot.java.demo.jooq.rest.api.module.order.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "order")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq", precision = 13, nullable = false)
    private Long seq;

    @Column(name = "code")
    private String code;

    @Column(name = "purchaser")
    private String purchaser;

    @Column(name = "seller")
    private String seller;

    @Column(name = "item")
    private String item;

    @Column(name = "price")
    private Long price;

    @Column(name = "cancel")
    private String cancel;

    @Column(name = "create_time")
    private String createTime;

    @Column(name = "update_time")
    private String updateTime;

}
