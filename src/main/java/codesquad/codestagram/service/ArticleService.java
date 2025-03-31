package codesquad.codestagram.service;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.repository.SpringDataJpaArticleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {

    private final SpringDataJpaArticleRepository articleRepository;

    public ArticleService(SpringDataJpaArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    // 게시글 등록
    public Long save(Article article) {
        articleRepository.save(article);
        return article.getId();
    }

    public Optional<Article> findOneArticle(Long id) {
        return articleRepository.findById(id);
    }

    public List<Article> findAllArticle() {
        return articleRepository.findAll();
    }
}
