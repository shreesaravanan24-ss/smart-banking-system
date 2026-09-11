package com.smartbanking.config;

import com.smartbanking.customer.entity.Customer;
import com.smartbanking.customer.repository.CustomerRepository;
import com.smartbanking.enums.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initializeAdmin(
            CustomerRepository customerRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            String adminEmail = "admin@smartbank.com";

            if (customerRepository.findByEmail(adminEmail).isEmpty()) {

                Customer admin = new Customer();

                admin.setFullName("System Administrator");
                admin.setEmail(adminEmail);
                admin.setPhone("9999999999");

                admin.setPassword(
                        passwordEncoder.encode("Admin@123")
                );

                admin.setRole(Role.ADMIN);

                customerRepository.save(admin);

                System.out.println(
                        "======================================"
                );
                System.out.println(
                        "DEFAULT ADMIN CREATED"
                );
                System.out.println(
                        "Email    : admin@smartbank.com"
                );
                System.out.println(
                        "Password : Admin@123"
                );
                System.out.println(
                        "======================================"
                );
            }
        };
    }
}