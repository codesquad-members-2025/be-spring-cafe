package codesquad.codestagram.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {  // ✅ 인터페이스로 변경!
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
//        registry.addViewController("/user/login_failed").setViewName("user/login_failed");
//        registry.addViewController("/user/signup-form").setViewName("user/form");
//        registry.addViewController("/qna/write-form").setViewName("qna/form");
//        registry.addViewController("/qna/show").setViewName("qna/show");
    }
}
