package config;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

@TestConfiguration
@EnableJpaRepositories(basePackages = "com.application.security.domain")
public class DataSourceTestConfig {

    @Bean("securityDataSource")
    @Primary
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .url(DatabaseContainerConfig.mysql.getJdbcUrl())
                .username(DatabaseContainerConfig.mysql.getUsername())
                .password(DatabaseContainerConfig.mysql.getPassword())
                .build();
    }

    @Bean("entityManagerFactory")
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factoryBean =
                new LocalContainerEntityManagerFactoryBean();

        JpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        factoryBean.setJpaVendorAdapter(adapter);

        factoryBean.setDataSource(dataSource());
        factoryBean.setPackagesToScan("com.application.security.domain");
        factoryBean.setPersistenceUnitName("persistence-unit");
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl", "none");
        factoryBean.setJpaProperties(properties);
        return factoryBean;
    }

    @Bean("transactionManager")
    public PlatformTransactionManager transactionManager(EntityManagerFactory factory) {
        return new JpaTransactionManager(factory);
    }
}
