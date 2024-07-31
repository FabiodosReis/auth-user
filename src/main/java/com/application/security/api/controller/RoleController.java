package com.application.security.api.controller;

import com.application.security.api.dto.*;
import com.application.security.domain.model.Role;
import com.application.security.domain.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService service;

    @PreAuthorize(value = "hasAnyAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void save(@RequestBody RoleRequestDto dto) {
        var role = new Role();
        role.setName(dto.getName());
        service.save(role);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CustomPage<?> findAll(@PageableDefault(size = 20, sort = {"name"}) Pageable pageable) {
        List<RoleResponseDTO> content = service.findAll(pageable)
                .stream().map(role -> RoleResponseDTO.builder().id(role.getId()).name(role.getName()).build())
                .toList();

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

    @GetMapping("/name/{roleName}")
    @ResponseStatus(HttpStatus.OK)
    public RoleResponseDTO findByRoleName(@PathVariable("roleName") String roleName) {
        Role role = service.findByName(roleName);

        return RoleResponseDTO.builder()
                .id(role.getId())
                .name(role.getName())
                .build();
    }
}
