package codesquad.codestagram.service;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.repository.ArticleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    // 게시글 등록
    public String ask(Article article) {
        articleRepository.save(article);
        return article.getWriter();
    }

    public Optional<Article> findOneArticle(String writer) {
        return articleRepository.findByWriter(writer);
    }

    public List<Article> findAllArticle() {
        return articleRepository.findAllArticles();
    }
}
