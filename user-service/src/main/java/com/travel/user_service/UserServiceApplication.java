package com.travel.user_service;

import com.travel.user_service.entity.User;
import com.travel.user_service.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class UserServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
    
    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository) {
        return args -> {
            if (userRepository.count() == 0) {
                userRepository.save(new User(null, "Harshana", "harshana@gamil.com", "0715256544", true));
                userRepository.save(new User(null, "Kasun", "kasunn@gmail.com", "0705236845", true));
                userRepository.save(new User(null, "Saman", "saman@gmail.com", "0745821564", true));
                System.out.println("User Service: Sample users created");
            } else {
                System.out.println("User Service: Sample users already exist");
            }
        };
    }
}