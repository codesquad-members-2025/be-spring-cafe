package codesquad.codestagram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CodestagramApplication {

    public static void main(String[] args) {
        SpringApplication.run(CodestagramApplication.class, args);
    }

}
