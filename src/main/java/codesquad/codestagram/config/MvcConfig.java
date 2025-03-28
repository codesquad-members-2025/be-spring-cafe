package codesquad.codestagram.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);  // MvcConfig 가 가장 먼저 실행되도록 우선순위 설정

        registry.addViewController("/user/form").setViewName("user/form");
        registry.addViewController("/user/login").setViewName("user/login");
//        registry.addViewController("/qna/form").setViewName("qna/form");
    }
}
