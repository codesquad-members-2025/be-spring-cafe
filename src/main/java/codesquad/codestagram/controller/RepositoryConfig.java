package codesquad.codestagram.controller;

import codesquad.codestagram.repository.userrepository.UserJpaRepository;
import codesquad.codestagram.repository.userrepository.UserMapRepository;
import codesquad.codestagram.repository.userrepository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {

    private final UserJpaRepository userJpaRepository;

    public RepositoryConfig(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Bean
    public UserRepository userRepository(){
        return userJpaRepository;
//        return new UserMapRepository();
    }
}