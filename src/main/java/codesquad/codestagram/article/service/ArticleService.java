package codesquad.codestagram.article.service;

import codesquad.codestagram.article.domain.Article;
import codesquad.codestagram.article.repository.ArticleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    //글쓰기
    public void write(Article article){
        articleRepository.save(article);
    }

    //게시글 목록 구현하기
    public List<Article> getAllArticles(){
        return articleRepository.findAll();
    }

    //게시글 상세보기 구현하기
    public Article getArticleById(Long Id){
        return articleRepository.findArticleByArticleId(Id);
    }

    public void updateArticle(Long id, Article updateArticle) {
        Article article = articleRepository.findArticleByArticleId(id);
        if(article != null){
            article.setTitle(updateArticle.getTitle());
            article.setContents(updateArticle.getContents());
            write(article);
        }
    }
}
