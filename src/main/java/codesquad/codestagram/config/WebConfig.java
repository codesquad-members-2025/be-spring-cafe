package codesquad.codestagram.config;

import codesquad.codestagram.interceptor.LoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor())
                .addPathPatterns(
                        "/questions",
                        "/boards/**",
                        "/users/**"
                )

                .excludePathPatterns(
                        "/",
                        "/users/login",
                        "/users/signup",
                        "/users",
                        "/css/**",
                        "/js/**",
                        "/images/**"
                );
    }
}
