package com.mrj.oauthdemo.entity;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

/**
 * @author MRJ
 * @date 2019/9/30 11:41
 * @Description: 角色实体类
 */
@Entity
@Table(name = "t_role")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Override
    public String getAuthority() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
