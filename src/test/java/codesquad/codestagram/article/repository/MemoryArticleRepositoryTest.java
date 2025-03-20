package codesquad.codestagram.article.repository;

import codesquad.codestagram.user.domain.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("MemoryArticleRepository: 질문 관리 기능 테스트")
public class MemoryArticleRepositoryTest {

    MemoryArticleRepository repository = new MemoryArticleRepository();

    @AfterEach
    void tearDown() {
        repository.clearStore();
    }

    @Test
    @DisplayName("새로운 Article 객체가 저장되면 동일한 객체를 조회할 수 있어야 한다.")
    void save() {
        // given: 테스트용 Article 객체 생성
        Article article = new Article(
                "글쓴이",
                "제목",
                "내용"
        );
        // when: Article 객체 저장
        repository.save(article);

        // then: 저장된 객체와 조회된 객체가 동일해야 함.
        Article findArticle = repository.findById(article.getId()).orElseThrow(
                () -> new IllegalStateException("존재하지 않는 질문입니다.")
        );

        assertThat(findArticle).isEqualTo(article);
    }

    @Test
    @DisplayName("저장되지 않은 Article 객체를 조회하면 IllegalStateException 예외가 발생해야 한다.")
    void findArticle() {
        // given: 테스트용 Article 객체 생성
        Article article = new Article(
                "글쓴이",
                "제목",
                "내용"
        );

        // when: 저장되지 않은 테스트용 Article의 아이디를 통해 조회할 때
        // then: IllegalStateException이 발생해야 함
        assertThatThrownBy(
                () -> repository.findById(article.getId()).orElseThrow(
                    () -> new IllegalStateException("존재하지 않는 질문입니다.")
                )
        ).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("저장된 모든 질문 목록을 올바르게 반환해야 한다.")
    void findAll() {
        // given: 테스트용 Article 객체 4개 생성 후 저장
        Article article1 = new Article("글쓴이1", "제목1", "내용1");
        Article article2 = new Article("글쓴이2", "제목2", "내용2");
        Article article3 = new Article("글쓴이3", "제목3", "내용3");
        Article article4 = new Article("글쓴이4", "제목4", "내용4");
        repository.save(article1);
        repository.save(article2);
        repository.save(article3);
        repository.save(article4);

        // when: 저장된 Article 객체 리스트 조회
        List<Article> result = repository.findAll();

        // then: 조회된 Article 객체 리스트가 테스트용 Article 리스트와 사이즈, 참조값이 동일해야 함
        assertThat(result).isEqualTo(new ArrayList<>(List.of(article1, article2, article3, article4)))
                .hasSize(4);
    }
}
