package com.ssafy.flowstudio.common.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.ssafy.flowstudio.domain",
        entityManagerFactoryRef = "primaryEntityManagerFactory",
        transactionManagerRef = "primaryTransactionManager"
)
public class PrimaryDbConfig {

    // Primary DataSource 설정
    @Primary
    @Bean(name = "primaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.primary")
    public DataSource primaryDataSource() {
        return new HikariDataSource();
    }

    @Primary
    @Bean(name = {"primaryEntityManagerFactory", "entityManagerFactory"})
    public LocalContainerEntityManagerFactoryBean primaryEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("primaryDataSource") DataSource primaryDataSource) {

        Map<String, Object> properties = new HashMap<>();
//        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        properties.put("hibernate.hbm2ddl.auto", "update"); // primary 데이터 소스의 ddl-auto 설정
        properties.put("hibernate.format_sql", true);
        properties.put("hibernate.show_sql", false);
        properties.put("hibernate.physical_naming_strategy", "org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy");
        properties.put("dbname", "primary");

        return builder
                .dataSource(primaryDataSource)
                .packages("com.ssafy.flowstudio.domain")  // primary 엔티티 경로
                .persistenceUnit("primary")
                .properties(properties)
                .build();
    }

    // Primary TransactionManager 설정
    @Primary
    @Bean(name = {"primaryTransactionManager", "transactionManager"})
    public PlatformTransactionManager primaryTransactionManager(
            @Qualifier("primaryEntityManagerFactory") LocalContainerEntityManagerFactoryBean primaryEntityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(primaryEntityManagerFactory.getObject());
        return transactionManager;
    }

    @Bean("multiTransactionManager")
    public PlatformTransactionManager multiTransactionManager(@Qualifier("primaryTransactionManager") PlatformTransactionManager primaryTransactionManager,
                                                              @Qualifier("secondaryTransactionManager") PlatformTransactionManager secondaryTransactionManager) {
        return new ChainedTransactionManager(primaryTransactionManager, secondaryTransactionManager);
    }

}
