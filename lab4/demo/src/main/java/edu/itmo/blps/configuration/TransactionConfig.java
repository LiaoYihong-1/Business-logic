package edu.itmo.blps.configuration;

import bitronix.tm.TransactionManagerServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;

/**
 * 1. @Transaction
 * 2. Make sure that data source is like what is used is spring jpa
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(transactionManagerRef = "bitronixTransactionManager", basePackages = "edu.itmo.blps.dao")
public class TransactionConfig {
    @Bean(name = "bitronixTransactionManager")
    public JtaTransactionManager bitronixTransactionManager() {
        JtaTransactionManager transactionManager = new JtaTransactionManager();
        transactionManager.setTransactionManager(TransactionManagerServices.getTransactionManager());
        return transactionManager;
    }
}