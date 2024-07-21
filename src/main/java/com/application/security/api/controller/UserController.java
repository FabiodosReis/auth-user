package com.application.security.api.controller;

import com.application.security.api.dto.*;
import com.application.security.domain.model.User;
import com.application.security.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/v1/authentication/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PreAuthorize(value = "hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping("register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody UserRequestDto user) {
        service.save(user.toEntity());
    }

    @PreAuthorize(value = "hasAnyAuthority('ROLE_ADMIN')")
    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public void update(@PathVariable("id") String id, @RequestBody UpdateRequestDto user) {
        service.update(user.toEntity(id));
    }

    @PreAuthorize(value = "hasAnyAuthority('ROLE_ADMIN')")
    @PutMapping("{id}/disable")
    @ResponseStatus(HttpStatus.OK)
    public void disable(@PathVariable("id") String id) {
        service.disable(id);
    }

    @PreAuthorize(value = "hasAnyAuthority('ROLE_USER')")
    @PutMapping("{id}/changePassword")
    @ResponseStatus(HttpStatus.OK)
    public void changePassword(@PathVariable("id") String id, @RequestBody ChangePasswordDto passwordDto) {
        service.changePassword(
                id,
                passwordDto.getNewPassword(),
                passwordDto.getConfirmPassword()
        );
    }

    @PreAuthorize(value = "hasAnyAuthority('ROLE_USER')")
    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserRequestDto findById(@PathVariable("id") String id) {
        return service.findById(id)
                .toRequestDto();
    }

    @PreAuthorize(value = "hasAnyAuthority('ROLE_USER')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CustomPage<?> findAll(@PageableDefault(size = 20, sort = {"name"}) Pageable pageable) {
        List<UserRequestDto> content = User.toRequestDtoList(service.findAll(pageable));

        int pageSize = pageable.getPageSize();
        int pageNumber = pageable.getPageNumber();
        long totalElements = content.size();

        CustomPageable customPageable = new CustomPageable(
                pageNumber,
                pageSize,
                totalElements
        );
        return new CustomPage<>(content, customPageable);
    }

    @PreAuthorize(value = "hasAnyAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("link-roles")
    public void linkRoles(@RequestBody RoleUserRequestDTO dto) {
        service.linkRoles(dto.getUser(), dto.getRoles());
    }

}