package codesquad.codestagram.config;

import codesquad.codestagram.repository.BoardRepository;
import codesquad.codestagram.repository.MemoryBoardRepository;
import codesquad.codestagram.repository.MemoryUserRepository;
import codesquad.codestagram.service.BoardService;
import codesquad.codestagram.service.UserService;
import codesquad.codestagram.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    public SpringConfig(UserRepository userRepository, BoardRepository boardRepository) {
        this.userRepository = userRepository;
        this.boardRepository = boardRepository;
    }

    @Bean
    public UserService userService() {
        return new UserService(userRepository);
    }

    @Bean
    public BoardService boardService() {
        return new BoardService(boardRepository);
    }

}
