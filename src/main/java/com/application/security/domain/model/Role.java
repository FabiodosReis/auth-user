package com.application.security.domain.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;

@Entity
@Table(name = "role")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter
@Builder
public class Role implements GrantedAuthority, Serializable {

    @EqualsAndHashCode.Include
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name")
    private String name;

    @Override
    public String getAuthority() {
        return "";
    }


}
