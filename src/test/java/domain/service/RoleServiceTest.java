package domain.service;

import com.application.security.domain.exception.RoleException;
import com.application.security.domain.model.Role;
import com.application.security.domain.repository.RoleRepository;
import com.application.security.domain.service.RoleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.application.security.domain.constants.RoleUserEnum.ROLE_USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {

    @InjectMocks
    private RoleService roleService;

    @Mock
    private RoleRepository roleRepository;

    @Captor
    private ArgumentCaptor<Role> roleArgumentCaptor;

    @Test
    void shouldSave() {
        var userRole = getRole(ROLE_USER.name());

        when(roleRepository.findByName(userRole.getName()))
                .thenReturn(Optional.empty());

        when(roleRepository.save(any()))
                .thenReturn(userRole);

        roleService.save(userRole);

        verify(roleRepository, times(1))
                .findByName(anyString());

        verify(roleRepository,times(1))
                .save(roleArgumentCaptor.capture());

        var roleSaved = roleArgumentCaptor.getValue();

        assertEquals(roleSaved.getName(),userRole.getName());

    }

    @Test
    void shouldNotSaveBecauseRoleAlreadyExists() {
        var userRole = getRole(ROLE_USER.name());

        Exception e = assertThrows(RoleException.class, () -> {
            when(roleRepository.findByName(userRole.getName()))
                    .thenReturn(Optional.of(userRole));

            roleService.save(userRole);

            verify(roleRepository, times(1))
                    .findByName(anyString());

            verify(roleRepository,times(0))
                    .save(any());
        });

        assertEquals("ROLE_USER already exists", e.getMessage());
    }

    @Test
    void shouldNotSaveBecauseRoleNameIsMissing() {
        var userRole = getRole(ROLE_USER.name());
        userRole.setName(null);

        Exception e = assertThrows(RoleException.class, () -> {
            roleService.save(userRole);

            verify(roleRepository, times(0))
                    .findByName(anyString());

            verify(roleRepository,times(0))
                    .save(any());
        });

        assertEquals("Role name is required", e.getMessage());
    }

    @Test
    void shouldFindByName(){
        var role = getRole(ROLE_USER.name());

        when(roleRepository.findByName(ROLE_USER.name()))
                .thenReturn(Optional.of(role));

        roleService.findByName(ROLE_USER.name());

        verify(roleRepository,times(1))
                .findByName(anyString());
    }

    @Test
    void shouldNotFindByName() {
        Exception e = assertThrows(RoleException.class, () -> {
            when(roleRepository.findByName(ROLE_USER.name()))
                    .thenReturn(Optional.empty());

            roleService.findByName(ROLE_USER.name());

            verify(roleRepository,times(1))
                    .findByName(anyString());
        });

        assertEquals("Role ROLE_USER not found.", e.getMessage());
    }

    @Test
    void shouldFindAll(){
        var role = getRole(ROLE_USER.name());

        var page = new PageImpl<>(
                List.of(role),
                Pageable.unpaged(),
                1
        );

        when(roleRepository.findAll(any(Pageable.class)))
                .thenReturn(page);

        roleService.findAll(Pageable.unpaged());

        verify(roleRepository, times(1))
                .findAll(any(Pageable.class));
    }


    private Role getRole(String roleName) {
        var role = new Role();
        role.setId("96f13969-9c5f-4088-acc7-f0506e5a66c9");
        role.setName(roleName);
        return role;
    }

}
