package repository;

import com.application.security.domain.repository.UserRepository;
import config.DataSourceTestConfig;
import config.DatabaseContainerConfig;
import config.LiquibaseTestConfig;
import config.RepositoryTest;
import org.junit.jupiter.api.Assertions;;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@RepositoryTest
@ContextConfiguration(
        classes = {LiquibaseTestConfig.class, DataSourceTestConfig.class, UserRepository.class},
        initializers = {DatabaseContainerConfig.class}
)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void shouldFindAllUsers() {
        Assertions.assertEquals(2, userRepository.findAll().size());
    }
}
