package io.github.yashchenkon.migration.spring;

import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.yashchenkon.migration.Migration;
import io.github.yashchenkon.migration.MigrationTask;
import io.github.yashchenkon.migration.registry.MigrationRegistry;
import io.github.yashchenkon.migration.registry.SimpleMigrationRegistry;
import io.github.yashchenkon.migration.repository.MigrationRepository;

/**
 * @author Mykola Yashchenko
 */
@Configuration
@ConditionalOnBean(MigrationRepository.class)
public class JavaMigrationToolSpringAutoConfiguration {

    @Bean(initMethod = "run")
    @ConditionalOnMissingBean(MigrationTask.class)
    public MigrationTask migrationTask(MigrationRepository migrationRepository,
                                       MigrationRegistry migrationRegistry) {
        return new MigrationTask(migrationRepository, migrationRegistry);
    }

    @Bean
    @ConditionalOnMissingBean(MigrationRegistry.class)
    public MigrationRegistry migrationRegistry(List<Migration> migrations) {
        return new SimpleMigrationRegistry(migrations);
    }

}
