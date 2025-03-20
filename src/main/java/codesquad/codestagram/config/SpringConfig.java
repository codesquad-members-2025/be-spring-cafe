package codesquad.codestagram.config;

import codesquad.codestagram.user.repository.MemoryUserRepository;
import codesquad.codestagram.user.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    @Bean
    public UserRepository userRepository() {
        return memoryUserRepository();
    }

    private MemoryUserRepository memoryUserRepository() {
        return new MemoryUserRepository();
    }
}
