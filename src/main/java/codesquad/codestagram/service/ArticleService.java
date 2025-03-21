package codesquad.codestagram.service;

import codesquad.codestagram.controller.Article;
import codesquad.codestagram.repository.ArticleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ArticleService {

    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public void save(Article article) {
        articleRepository.save(article);
    }

    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    public Article findByIndex(Long index){
        return articleRepository.findByIndex(index)
                .orElseThrow(() -> new RuntimeException("해당 질문을 찾을 수 없습니다."));
    }

}
