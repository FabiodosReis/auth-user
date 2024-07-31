package config;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@TestConfiguration
public class LiquibaseTestConfig {
    @Bean
    public SpringLiquibase liquibase(@Qualifier("securityDataSource") DataSource dataSource){
        var liquibase = new SpringLiquibase();
        liquibase.setDropFirst(true);
        liquibase.setDataSource(dataSource);
        liquibase.setDefaultSchema("test");
        liquibase.setChangeLog("classpath:/db/changelog.xml");
        return liquibase;
    }
}
