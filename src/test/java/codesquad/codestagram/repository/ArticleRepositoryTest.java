package codesquad.codestagram.repository;

import codesquad.codestagram.domain.Article;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ArticleRepositoryTest {

    private ArticleRepository articleRepository;

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
}
