package codesquad.codestagram;

import codesquad.codestagram.repository.MemoryUserRepository;
import codesquad.codestagram.service.UserService;
import codesquad.codestagram.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {
    @Bean
    public UserService userService() {
        return new UserService(userRepository());
    }
    @Bean
    public UserRepository userRepository() {
        return new MemoryUserRepository();
    }

}
