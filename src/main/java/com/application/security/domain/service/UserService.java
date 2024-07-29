package com.application.security.domain.service;

import com.application.security.domain.exception.UserException;
import com.application.security.domain.repository.RoleRepository;
import com.application.security.domain.model.Role;
import com.application.security.domain.model.User;
import com.application.security.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static com.application.security.domain.constants.RoleUserEnum.ROLE_USER;
import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserService implements UserDetailsService {

    private final UserRepository repository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void changePassword(
            String userId,
            String newPassword,
            String repeatPassword) {

        passwordValidation(newPassword, repeatPassword);

        User user = findById(userId);

        user.setPassword(passwordEncoder.encode(newPassword));

        repository.save(user);

    }

    @Transactional
    public void save(User user) throws UserException {
        List<Role> roles = new ArrayList<>();

        validation(user);

        repository.findByEmail(user.getEmail())
                .ifPresent(findUser -> {
                    throw new UserException(String.format("Email %s already exist", user.getEmail()));
                });

        user.setEnable(true);
        user.setCreateDate(LocalDateTime.now(ZoneId.of("UTC")));
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        roleRepository.findByName(ROLE_USER.name())
                .ifPresent(roles::add);

        user.setRoles(roles);
        repository.save(user);

        log.info("User save successfully");
    }

    @Transactional
    public void update(User user) {
        User currentUser = findById(user.getId());

        if (isNotEmpty(user) || isNotEmpty(user.getName())) {
            currentUser.setName(user.getName());
        }

        if (isNotEmpty(user) || isNotEmpty(user.getEmail())) {
            currentUser.setEmail(user.getEmail());
        }

        validation(currentUser);

        repository.findByEmailAndId(currentUser.getEmail(), currentUser.getId())
                .ifPresent(findUser -> {
                    throw new UserException(String.format("Email %s already exist", user.getEmail()));
                });

        repository.save(currentUser);

        log.info("User updated successfully");

    }

    private void validation(User user) throws UserException {
        if (isEmpty(user) || isEmpty(user.getEmail())) {
            throw new UserException("Email is required");
        }

        if (isEmpty(user) || isEmpty(user.getName())) {
            throw new UserException("Name is required");
        }

        if (isEmpty(user) || isEmpty(user.getPassword())) {
            throw new UserException("Password is required");
        }

        log.info("User is valid");

    }

    @Transactional
    public void disable(String id) {
        repository.findById(id)
                .ifPresent(currentUser -> {
                    currentUser.setEnable(false);
                    repository.save(currentUser);
                });
    }

    public User findById(String id) throws UserException {
        return repository.findById(id)
                .orElseThrow(() -> new UserException(String.format("User %s not found.", id)));
    }

    public List<User> findAll(Pageable pageable) throws UserException {
        return repository.findAll(pageable).getContent();
    }

    public User findByEmail(String email) throws UserException {
        return repository.findByEmail(email)
                .orElseThrow(() -> new UserException(String.format("User %s not found.", email)));
    }

    private void passwordValidation(String newPassword, String repeatPassword) throws UserException {
        if (isEmpty(newPassword)) {
            throw new UserException("New Password is required");
        }

        if (isEmpty(repeatPassword)) {
            throw new UserException("Confirm password is required");
        }

        if (!newPassword.equals(repeatPassword)) {
            throw new UserException("Password and confirm password do not matched");
        }

        if (newPassword.length() < 6) {
            throw new UserException("Password must be minimal 6 characters");
        }
    }


    @Transactional
    public void linkRoles(String userId, List<String> roleIds){
        User user = findById(userId);
        List<Role> roles = new ArrayList<>();

        roleIds.forEach(roleId -> roleRepository.findById(roleId).ifPresent(roles::add));
        roles.addAll(user.getRoles());

        user.setRoles(roles);
        repository.save(user);

        log.info("link roles successfully");
    }

    //load user and role in order to generate token
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return findByEmail(email);
    }
}
