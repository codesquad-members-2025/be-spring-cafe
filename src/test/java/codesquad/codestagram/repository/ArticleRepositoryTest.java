package codesquad.codestagram.repository;

import codesquad.codestagram.domain.Article;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class ArticleRepositoryTest {

    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleRepositoryTest(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Test
    @DisplayName("1번 글이 작성됩니다.")
    @Transactional
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
    @Transactional
    void findAll() {
        //Given
        Article firstArticle = new Article();
        firstArticle.setTitle("안녕하세요");
        firstArticle.setContent("반갑습니다.");
        articleRepository.save(firstArticle);

        Article secoundArticle = new Article();
        secoundArticle.setTitle("반갑습니다.");
        secoundArticle.setContent("안녕하세요.");
        articleRepository.save(secoundArticle);

        //When
        List<Article> articles = articleRepository.findAll();

        //Then
        assertThat(articles.size()).isEqualTo(2);
    }
}
