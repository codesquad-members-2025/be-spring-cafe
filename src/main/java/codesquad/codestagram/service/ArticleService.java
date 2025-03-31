package codesquad.codestagram.service;

import codesquad.codestagram.controller.Article;
import codesquad.codestagram.controller.User;
import codesquad.codestagram.dto.RequestArticleDto;
import codesquad.codestagram.repository.ArticleRepository;
import codesquad.codestagram.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    public ArticleService(ArticleRepository articleRepository, UserRepository userRepository) {

        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    public void save(RequestArticleDto requestArticleDto) {
        User user = userRepository.findById(requestArticleDto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("해당 유저를 찾을 수 없습니다."));
        Article article = requestArticleDto.toArticle(user);
        articleRepository.save(article);
    }

    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    public Article findById(Long id){
        return articleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 질문을 찾을 수 없습니다."));
    }

}
