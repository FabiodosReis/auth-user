package domain.service;


import com.application.security.domain.exception.UserException;
import com.application.security.domain.model.Role;
import com.application.security.domain.model.User;
import com.application.security.domain.repository.RoleRepository;
import com.application.security.domain.repository.UserRepository;
import com.application.security.domain.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.application.security.domain.constants.RoleUserEnum.ROLE_ADMIN;
import static com.application.security.domain.constants.RoleUserEnum.ROLE_USER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService service;

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Captor
    private ArgumentCaptor<User> argumentCaptorUser;

    @Test
    public void shouldChangePassword() {
        var user = getUser();
        var userID = "cf5345b5-2aaf-49eb-823d-66e5431c2f9b";
        var newPassword = "123123";
        var repeatPassword = "123123";

        when(userRepository.findById(eq(userID)))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.encode(eq(newPassword)))
                .thenReturn("******");

        when(userRepository.save(any()))
                .thenReturn(user);

        service.changePassword(
                userID,
                newPassword,
                repeatPassword
        );

        verify(userRepository, times(1))
                .findById(userID);

        verify(passwordEncoder, times(1))
                .encode(newPassword);

        verify(userRepository, times(1))
                .save(argumentCaptorUser.capture());

        var userSaved = argumentCaptorUser.getValue();

        assertEquals("cf5345b5-2aaf-49eb-823d-66e5431c2f9b", userSaved.getId());
        assertEquals("test@gmail.com", userSaved.getEmail());
        assertEquals("Test", userSaved.getName());
        assertEquals(LocalDateTime.of(2024, 6, 1, 0, 0, 0), userSaved.getCreateDate());
        assertTrue(userSaved.isEnabled());
        assertEquals("******", userSaved.getPassword());

    }

    @Test
    public void shouldNotChangePasswordBecauseNewPasswordIsRequired() {
        var user = getUser();

        var exception = assertThrows(UserException.class, () -> service.changePassword(
                user.getId(),
                null,
                null
        ));

        assertEquals("New Password is required", exception.getMessage());

        verify(userRepository, times(0))
                .save(any());
    }

    @Test
    public void shouldNotChangePasswordBecauseConfirmPasswordIsRequired() {
        var user = getUser();

        var exception = assertThrows(UserException.class, () -> service.changePassword(
                user.getId(),
                "123786",
                null
        ));

        assertEquals("Confirm password is required", exception.getMessage());

        verify(userRepository, times(0))
                .save(any());
    }

    @Test
    public void shouldNotChangePasswordBecausePasswordAndConfirmPasswordNotMatched() {
        var user = getUser();

        var exception = assertThrows(UserException.class, () -> service.changePassword(
                user.getId(),
                "123456",
                "098765"
        ));

        assertEquals("Password and confirm password do not matched", exception.getMessage());

        verify(userRepository, times(0))
                .save(any());
    }

    @Test
    public void shouldNotChangePasswordBecausePasswordLessThenSixDigits() {
        var user = getUser();
        var newPassword = "123";
        var repeatPassword = "123";

        var exception = assertThrows(UserException.class, () -> service.changePassword(
                user.getId(),
                newPassword,
                repeatPassword
        ));

        assertEquals("Password must be minimal 6 characters", exception.getMessage());

        verify(userRepository, times(0))
                .save(any());
    }

    @Test
    public void shouldSaveUser() {
        var user = getUser();
        var userRole = getUserRole();

        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode(eq(user.getPassword())))
                .thenReturn("******");

        when(roleRepository.findByName(eq(ROLE_USER.name())))
                .thenReturn(Optional.of(userRole));

        when(userRepository.save(any()))
                .thenReturn(user);

        service.save(user);

        verify(userRepository, times(1))
                .findByEmail(user.getEmail());

        verify(passwordEncoder, times(1))
                .encode("998766");

        verify(roleRepository, times(1))
                .findByName(ROLE_USER.name());

        verify(userRepository, times(1))
                .save(argumentCaptorUser.capture());

        var userSaved = argumentCaptorUser.getValue();

        assertEquals("cf5345b5-2aaf-49eb-823d-66e5431c2f9b", userSaved.getId());
        assertEquals("test@gmail.com", userSaved.getEmail());
        assertEquals("Test", userSaved.getName());
        assertTrue(userSaved.isEnabled());
        assertEquals("******", userSaved.getPassword());
        assertTrue(userSaved.getRoles().stream().anyMatch(role -> role.getName().equals(ROLE_USER.name())));
        assertTrue(userSaved.getRoles().stream().anyMatch(role -> role.getId().equals("df1234400-2aaf-49eb-gh34-05e543162f91")));
    }

    @Test
    public void shouldNotSaveUserBecauseEmailIsRequiredExists() {
        var user = getUser();
        user.setEmail(null);

        var exception = assertThrows(UserException.class, () -> service.save(user));

        assertEquals("Email is required", exception.getMessage());

        verify(userRepository, times(0))
                .save(any());
    }

    @Test
    public void shouldNotSaveUserBecauseNameIsRequiredExists() {
        var user = getUser();
        user.setName(null);

        var exception = assertThrows(UserException.class, () -> service.save(user));

        assertEquals("Name is required", exception.getMessage());

        verify(userRepository, times(0))
                .save(any());
    }

    @Test
    public void shouldNotSaveUserBecausePasswordIsRequiredExists() {
        var user = getUser();
        user.setPassword(null);

        var exception = assertThrows(UserException.class, () -> service.save(user));

        assertEquals("Password is required", exception.getMessage());

        verify(userRepository, times(0))
                .save(any());
    }

    @Test
    public void shouldNotSaveUserBecauseEmailAlreadyExists() {
        var user = getUser();

        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        var exception = assertThrows(UserException.class, () -> service.save(user));

        assertEquals("Email test@gmail.com already exist", exception.getMessage());

        verify(userRepository, times(1))
                .findByEmail(user.getEmail());

        verify(userRepository, times(0))
                .save(any());
    }

    @Test
    public void shouldUpdateUser() {
        var user = getUser();
        var userUpdate = new User();
        userUpdate.setId("cf5345b5-2aaf-49eb-823d-66e5431c2f9b");
        userUpdate.setName("user update");
        userUpdate.setEmail("userUpdate@gmail.com");
        userUpdate.setCreateDate(LocalDateTime.of(2024, 6, 1, 0, 0, 0));
        userUpdate.setEnable(true);
        userUpdate.setPassword("998766");

        when(userRepository.findById(eq(user.getId())))
                .thenReturn(Optional.of(user));

        when(userRepository.findByEmailAndId(eq("userUpdate@gmail.com"), eq("cf5345b5-2aaf-49eb-823d-66e5431c2f9b")))
                .thenReturn(Optional.empty());

        when(userRepository.save(any()))
                .thenReturn(userUpdate);


        service.update(userUpdate);


        verify(userRepository, times(1))
                .findById(user.getId());

        verify(userRepository, times(1))
                .findByEmailAndId(userUpdate.getEmail(), userUpdate.getId());

        verify(userRepository, times(1))
                .save(argumentCaptorUser.capture());

        var userUpdated = argumentCaptorUser.getValue();

        assertEquals("cf5345b5-2aaf-49eb-823d-66e5431c2f9b", userUpdated.getId());
        assertEquals("userUpdate@gmail.com", userUpdated.getEmail());
        assertEquals("user update", userUpdated.getName());
        assertEquals(LocalDateTime.of(2024, 6, 1, 0, 0, 0), userUpdated.getCreateDate());
        assertTrue(userUpdated.isEnabled());
        assertEquals("998766", userUpdated.getPassword());
    }

    @Test
    public void shouldNotUpdateUserEmailAlreadyExists() {
        var user = getUser();
        var userUpdate = new User();
        userUpdate.setId("cf5345b5-2aaf-49eb-823d-66e5431c2f9b");
        userUpdate.setName("user update");
        userUpdate.setEmail("userUpdateEmail@gmail.com");
        userUpdate.setCreateDate(LocalDateTime.of(2024, 6, 1, 0, 0, 0));
        userUpdate.setEnable(true);
        userUpdate.setPassword("998766");

        when(userRepository.findById(eq(user.getId())))
                .thenReturn(Optional.of(user));

        when(userRepository.findByEmailAndId(eq("userUpdateEmail@gmail.com"), eq("cf5345b5-2aaf-49eb-823d-66e5431c2f9b")))
                .thenReturn(Optional.of(userUpdate));


        var exception = assertThrows(UserException.class, () -> service.update(userUpdate));

        assertEquals("Email userUpdateEmail@gmail.com already exist", exception.getMessage());

        verify(userRepository, times(1))
                .findById(user.getId());

        verify(userRepository, times(1))
                .findByEmailAndId(userUpdate.getEmail(), userUpdate.getId());

        verify(userRepository, times(0))
                .save(any());
    }


    @Test
    public void shouldDisableUser() {
        var user = getUser();

        when(userRepository.findById(eq(user.getId())))
                .thenReturn(Optional.of(user));

        when(userRepository.save(any()))
                .thenReturn(user);


        service.disable(user.getId());


        verify(userRepository, times(1))
                .findById(user.getId());

        verify(userRepository, times(1))
                .save(argumentCaptorUser.capture());

        var userDisabled = argumentCaptorUser.getValue();

        assertEquals("cf5345b5-2aaf-49eb-823d-66e5431c2f9b", userDisabled.getId());
        assertEquals("test@gmail.com", userDisabled.getEmail());
        assertEquals("Test", userDisabled.getName());
        assertFalse(userDisabled.isEnabled());
        assertEquals("998766", userDisabled.getPassword());
    }

    @Test
    public void shouldFindUserById() {
        var user = getUser();

        when(userRepository.findById(eq(user.getId())))
                .thenReturn(Optional.of(user));


        User currentUser = service.findById(user.getId());


        verify(userRepository, times(1))
                .findById(user.getId());

        assertEquals("cf5345b5-2aaf-49eb-823d-66e5431c2f9b", currentUser.getId());
        assertEquals("test@gmail.com", currentUser.getEmail());
        assertEquals("Test", currentUser.getName());
        assertTrue(currentUser.isEnabled());
        assertEquals(LocalDateTime.of(2024, 6, 1, 0, 0, 0), currentUser.getCreateDate());
        assertEquals("998766", currentUser.getPassword());
    }

    @Test
    public void shouldFindUserByEmail() {
        var user = getUser();

        when(userRepository.findByEmail(eq(user.getEmail())))
                .thenReturn(Optional.of(user));


        User currentUser = service.findByEmail(user.getEmail());


        verify(userRepository, times(1))
                .findByEmail(user.getEmail());

        assertEquals("cf5345b5-2aaf-49eb-823d-66e5431c2f9b", currentUser.getId());
        assertEquals("test@gmail.com", currentUser.getEmail());
        assertEquals("Test", currentUser.getName());
        assertTrue(currentUser.isEnabled());
        assertEquals(LocalDateTime.of(2024, 6, 1, 0, 0, 0), currentUser.getCreateDate());
        assertEquals("998766", currentUser.getPassword());
    }

    @Test
    public void shouldNotFindUserById() {
        var exception = assertThrows(UserException.class, () -> {
            when(userRepository.findById(anyString()))
                    .thenReturn(Optional.empty());

            service.findById("cf5345b5-2aaf-49eb-823d-66e5431c2f9b");
        });

        assertEquals("User cf5345b5-2aaf-49eb-823d-66e5431c2f9b not found.", exception.getMessage());
    }

    @Test
    public void shouldNotFindUserByEmail() {
        var exception = assertThrows(UserException.class, () -> {
            when(userRepository.findByEmail(anyString()))
                    .thenReturn(Optional.empty());

            service.findByEmail("test@gmail.com");
        });
        assertEquals("User test@gmail.com not found.", exception.getMessage());
    }

    @Test
    public void shouldFindAllUsers() {
        var user = getUser();

        var page = new PageImpl<>(
                List.of(user),
                Pageable.unpaged(),
                1
        );

        when(userRepository.findAll(any(Pageable.class)))
                .thenReturn(page);

        List<User> users = service.findAll(Pageable.unpaged());

        assertEquals(1, users.size());

        verify(userRepository, times(1))
                .findAll(any(Pageable.class));
    }

    @Test
    public void shouldLinkRoles() {
        var user = getUser();
        var userRole = new Role();
        userRole.setId("225345b5-2aaf-49eb-823d-66e5431c2f88");
        userRole.setName(ROLE_USER.name());
        user.setRoles(List.of(userRole));
        var adminRole = new Role();
        adminRole.setId("cf5345b5-2aaf-49eb-823d-66e5431c2f9b");
        adminRole.setName(ROLE_ADMIN.name());


        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));

        when(roleRepository.findById(adminRole.getId()))
                .thenReturn(Optional.of(adminRole));

        when(userRepository.save(any()))
                .thenReturn(user);


        service.linkRoles(user.getId(), List.of(adminRole.getId()));


        verify(userRepository, times(1))
                .findById(user.getId());

        verify(roleRepository, times(1))
                .findById(adminRole.getId());

        verify(userRepository, times(1))
                .save(argumentCaptorUser.capture());

        var userSaved = argumentCaptorUser.getValue();

        assertEquals("cf5345b5-2aaf-49eb-823d-66e5431c2f9b", userSaved.getId());
        assertEquals("test@gmail.com", userSaved.getEmail());
        assertEquals("Test", userSaved.getName());
        assertTrue(userSaved.isEnabled());
        assertEquals("998766", userSaved.getPassword());
        assertEquals(2, user.getRoles().size());
        assertTrue(userSaved.getRoles().stream().anyMatch(role -> role.getName().equals(ROLE_USER.name())));
        assertTrue(userSaved.getRoles().stream().anyMatch(role -> role.getName().equals(ROLE_ADMIN.name())));

    }

    @Test
    public void shouldLoadUserByName() {
        var user = getUser();

        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(user));

        service.loadUserByUsername(user.getEmail());

        verify(userRepository, times(1))
                .findByEmail(anyString());

    }


    private User getUser() {
        var user = new User();
        user.setId("cf5345b5-2aaf-49eb-823d-66e5431c2f9b");
        user.setEmail("test@gmail.com");
        user.setName("Test");
        user.setRoles(Collections.emptyList());
        user.setCreateDate(LocalDateTime.of(2024, 6, 1, 0, 0, 0));
        user.setEnable(true);
        user.setPassword("998766");
        return user;
    }

    private Role getUserRole() {
        var role = new Role();
        role.setId("df1234400-2aaf-49eb-gh34-05e543162f91");
        role.setName(ROLE_USER.name());
        return role;
    }

}
