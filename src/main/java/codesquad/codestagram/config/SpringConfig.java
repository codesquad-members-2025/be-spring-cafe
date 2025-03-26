package codesquad.codestagram.config;

import codesquad.codestagram.repository.JpaUserRepository;
import codesquad.codestagram.repository.UserRepository;
import codesquad.codestagram.service.UserService;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    private EntityManager em;

    @Autowired
    public SpringConfig(EntityManager em) {
        this.em = em;
    }

    @Bean
    public UserRepository userRepository() {
        return new JpaUserRepository(em);
    }
}
