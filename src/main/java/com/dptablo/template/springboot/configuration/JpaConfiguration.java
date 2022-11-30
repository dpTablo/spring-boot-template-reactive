package com.dptablo.template.springboot.configuration;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Optional;
import java.util.Properties;

@Configuration
@EntityScan(basePackages = {"com.dptablo.template.springboot.model.entity"})
@EnableJpaRepositories(
        basePackages = {"com.dptablo.template.springboot.repository.jpa"},
        entityManagerFactoryRef = "postgresEntityManager",
        transactionManagerRef = "postgresTransactionManager"
)
@EnableTransactionManagement
@RequiredArgsConstructor
public class JpaConfiguration {
    private final Environment env;

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource-postgres")
    public DataSourceProperties postgresProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "postgresDataSource")
    @Primary
    public DataSource postgresDataSource(DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean(name = "postgresEntityManager")
    @Primary
    public LocalContainerEntityManagerFactoryBean postgresEntityManager(){
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();

        HibernateJpaVendorAdapter hbAdapter = new HibernateJpaVendorAdapter();
        entityManagerFactoryBean.setJpaVendorAdapter(hbAdapter);

        entityManagerFactoryBean.setDataSource(postgresDataSource(postgresProperties()));
        entityManagerFactoryBean.setPackagesToScan(
                "com.dptablo.template.springboot.model.entity"); // database entity package path
        entityManagerFactoryBean.setJpaProperties(jpaProperties());

        return entityManagerFactoryBean;
    }

    private Properties jpaProperties() {
        Properties properties = new Properties();
        final String rootPath = "spring.jpa.properties.hibernate";

        var physicalNamingStrategy = env.getProperty("spring.jpa.hibernate.naming.physical-strategy");
        physicalNamingStrategy = physicalNamingStrategy == null ?
                CamelCaseToUnderscoresNamingStrategy.class.getName() : physicalNamingStrategy;
//                PhysicalNamingStrategyStandardImpl.class.getName() : physicalNamingStrategy;
        properties.put("hibernate.physical_naming_strategy", physicalNamingStrategy);

        var implicitNamingStrategy = env.getProperty("spring.jpa.hibernate.naming.implicit-strategy");
        implicitNamingStrategy = implicitNamingStrategy == null ?
                SpringImplicitNamingStrategy.class.getName() : implicitNamingStrategy;
        properties.put("hibernate.implicit_naming_strategy", implicitNamingStrategy);

        properties.setProperty("hibernate.hbm2ddl.auto",
                Optional.ofNullable(env.getProperty(rootPath + ".hbm2ddl.auto")).orElse("validate"));
        properties.setProperty("hibernate.hibernate.dialect",
                Optional.ofNullable(env.getProperty(rootPath + ".dialect"))
                        .orElse("org.hibernate.dialect.PostgreSQLDialect"));
        properties.setProperty("hibernate.show_sql",
                Optional.ofNullable(env.getProperty(rootPath + ".show_sql")).orElse("true"));
        properties.setProperty("hibernate.format_sql",
                Optional.ofNullable(env.getProperty(rootPath + ".format_sql")).orElse("true"));
        return properties;
    }

    @Bean(name = "postgresTransactionManager")
    @Primary
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }
}
