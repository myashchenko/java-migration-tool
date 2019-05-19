package io.github.yashchenkon.migration.spring;

import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.yashchenkon.migration.core.model.Migration;
import io.github.yashchenkon.migration.core.MigrationTask;
import io.github.yashchenkon.migration.core.registry.SimpleMigrationRegistry;
import io.github.yashchenkon.migration.core.lock.MigrationLock;
import io.github.yashchenkon.migration.core.registry.MigrationRegistry;
import io.github.yashchenkon.migration.core.repository.MigrationRepository;

/**
 * @author Mykola Yashchenko
 */
@Configuration
@ConditionalOnBean(MigrationRepository.class)
public class JavaMigrationToolSpringAutoConfiguration {

    @Bean(initMethod = "run")
    @ConditionalOnMissingBean(MigrationTask.class)
    public MigrationTask migrationTask(MigrationRepository migrationRepository,
                                       MigrationRegistry migrationRegistry,
                                       MigrationLock migrationLock) {
        return new MigrationTask(migrationRepository, migrationRegistry, migrationLock);
    }

    @Bean
    @ConditionalOnMissingBean(MigrationRegistry.class)
    public MigrationRegistry migrationRegistry(List<Migration> migrations) {
        return new SimpleMigrationRegistry(migrations);
    }

}
