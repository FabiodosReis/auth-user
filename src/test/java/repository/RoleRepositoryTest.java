package repository;


import com.application.security.domain.repository.RoleRepository;
import config.DataSourceTestConfig;
import config.DatabaseContainerConfig;
import config.LiquibaseTestConfig;
import annotation.TestRepositoryCommonAnnotation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;


@TestRepositoryCommonAnnotation
@ContextConfiguration(
        classes = {LiquibaseTestConfig.class, DataSourceTestConfig.class, RoleRepository.class},
        initializers = {DatabaseContainerConfig.class}
)
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void shouldFindAllUsers() {
        Assertions.assertEquals(2, roleRepository.findAll().size());
    }
}
