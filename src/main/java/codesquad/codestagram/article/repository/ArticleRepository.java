package codesquad.codestagram.article.repository;

import codesquad.codestagram.article.domain.Article;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;


public interface ArticleRepository {

    //글쓰기
    Article save(Article article);

    //글 목록 조회하기
    List<Article> getAllArticles();
}
