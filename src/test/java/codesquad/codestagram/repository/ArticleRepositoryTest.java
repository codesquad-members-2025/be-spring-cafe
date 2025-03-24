package codesquad.codestagram.repository;

import codesquad.codestagram.domain.Article;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ArticleRepositoryTest {

    private ArticleRepository articleRepository;

    public ArticleRepositoryTest(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Test
    @DisplayName("1번 글이 작성됩니다.")
    void save() {
        //Given
        Article article = new Article();
        article.setTitle("안녕하세요");
        article.setContent("반갑습니다.");

        //When
        articleRepository.save(article);
        Article expected = articleRepository.findById(1L).orElseThrow();

        //Then
        assertThat(article.getId()).isEqualTo(expected.getId());
    }

    @Test
    @DisplayName("모든 글의 갯수는 2개입니다.")
    void findAll() {
        //Given
        Article article1 = new Article();
        article1.setTitle("안녕하세요");
        article1.setContent("반갑습니다.");
        articleRepository.save(article1);

        Article article2 = new Article();
        article2.setTitle("반갑습니다.");
        article2.setContent("안녕하세요.");
        articleRepository.save(article2);

        //When
        List<Article> articles = articleRepository.findAll();

        //Then
        assertThat(articles.size()).isEqualTo(2);
    }
}
