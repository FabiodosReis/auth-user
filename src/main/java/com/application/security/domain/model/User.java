package com.application.security.domain.model;

import com.application.security.api.dto.UserRequestDto;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.application.security.domain.constants.RoleUserEnum.ROLE_ADMIN;
import static com.application.security.domain.constants.RoleUserEnum.ROLE_USER;


@Entity
@Table(name = "user")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User implements UserDetails {

    @EqualsAndHashCode.Include
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "email")
    private String email;

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createDate;

    @Column(name = "enable")
    private boolean enable;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "roleId")
    )
    private List<Role> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.roles.stream().map(Role::getName).toList().contains(ROLE_ADMIN.name())) {
            return List.of(new SimpleGrantedAuthority(ROLE_ADMIN.name()), new SimpleGrantedAuthority(ROLE_USER.name()));
        }
        return List.of(new SimpleGrantedAuthority(ROLE_USER.name()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enable;
    }

    public UserRequestDto toRequestDto() {
        return UserRequestDto.builder()
                .email(email)
                .name(name)
                .enable(enable)
                .roles(this.roles.stream().map(Role::getName).collect(Collectors.toList()))
                .build();
    }

    public static List<UserRequestDto> toRequestDtoList(List<User> users) {
        List<UserRequestDto> list = new ArrayList<>();
        users.forEach(user -> {
            list.add(user.toRequestDto());

        });
        return list;
    }
}
