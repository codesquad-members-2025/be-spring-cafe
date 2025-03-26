package codesquad.codestagram.config;

import codesquad.codestagram.repository.user.UserJpaRepository;
import codesquad.codestagram.repository.user.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfig {

    private final UserJpaRepository userJpaRepository;

    public UserConfig(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Bean
    public UserRepository userRepository() {
        return userJpaRepository;
//        return new UserMapRepository();
    }
}