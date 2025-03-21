package codesquad.codestagram.article.repository;

import codesquad.codestagram.article.domain.Article;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;


public interface ArticleRepository {

    //글쓰기
    Article save(Article article);
}
