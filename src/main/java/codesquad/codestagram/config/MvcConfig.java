package codesquad.codestagram.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);

        // users URL
        registry.addViewController("/users/join").setViewName("user/form");
//        registry.addViewController("/users/login").setViewName("user/login");
//        registry.addViewController("/users/login-failed").setViewName("user/login-failed");

        // questions URL
        registry.addViewController("/questions/form").setViewName("qna/form");
    }
}
