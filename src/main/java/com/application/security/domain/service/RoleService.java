package com.application.security.domain.service;

import com.application.security.domain.exception.RoleException;
import com.application.security.domain.repository.RoleRepository;
import com.application.security.domain.model.Role;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository repository;

    @Transactional
    public void save(Role role) throws RoleException {
        validation(role);
        repository.save(role);
    }

    public Role findByName(String name) throws RoleException {
      return repository.findByName(name)
              .orElseThrow(() -> new RoleException(String.format("Role %s not found.", name)));
    }

    private void validation(Role role) throws RoleException {

        if (isEmpty(role.getName())) {
            throw new RoleException("Role name is required");
        }

        repository.findByName(role.getName())
                .ifPresent(c -> {
                    throw new RoleException(String.format("%s already exists", role.getName()));
                });
    }

    public List<Role> findAll(Pageable pageable){
        return repository.findAll(pageable).getContent();
    }
}
