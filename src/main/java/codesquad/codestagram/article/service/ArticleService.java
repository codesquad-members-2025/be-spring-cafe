package codesquad.codestagram.article.service;

import codesquad.codestagram.article.domain.Article;
import codesquad.codestagram.article.dto.ArticleRequest;
import codesquad.codestagram.article.repository.ArticleRepository;
import codesquad.codestagram.user.domain.User;
import codesquad.codestagram.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

import static codesquad.codestagram.user.service.UserService.USER_NOT_FOUND;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    public ArticleService(ArticleRepository articleRepository, UserRepository userRepository) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Long create(ArticleRequest request, Long id) {

        User foundUser = userRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException(USER_NOT_FOUND)
        );

        Article article = new Article(
                foundUser,
                request.title(),
                request.contents()
        );
        return articleRepository.save(article).getId();
    }

    public List<Article> findArticles() {
        return articleRepository.findAll();
    }

    public Article findArticle(Long id) {
        return articleRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("존재하지 않는 질문입니다.")
        );
    }

}
