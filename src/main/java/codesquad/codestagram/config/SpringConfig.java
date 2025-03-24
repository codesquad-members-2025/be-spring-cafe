package codesquad.codestagram.config;

import codesquad.codestagram.article.repository.ArticleRepository;
import codesquad.codestagram.article.repository.impl.MemoryArticleRepository;
import codesquad.codestagram.user.repository.impl.MemoryUserRepository;
import codesquad.codestagram.user.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    @Bean
    public UserRepository userRepository() {
        return memoryUserRepository();
    }

    @Bean
    public ArticleRepository articleRepository() {
        return memoryArticleRepository();
    }

    private MemoryArticleRepository memoryArticleRepository() {
        return new MemoryArticleRepository();
    }

    private MemoryUserRepository memoryUserRepository() {
        return new MemoryUserRepository();
    }
}
