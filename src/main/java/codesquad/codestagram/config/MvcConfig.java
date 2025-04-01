package codesquad.codestagram.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
//        registry.addViewController("/").setViewName("index"); // index.html로 매핑
        registry.addViewController("/users/new").setViewName("user/form");
//        registry.addViewController("/users/login").setViewName("user/login");
        registry.addViewController("/users/login_failed").setViewName("user/login_failed");
        registry.addViewController("/boards/new").setViewName("qna/form");

    }
}
