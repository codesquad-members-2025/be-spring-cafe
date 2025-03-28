package codesquad.codestagram.service;

import static org.assertj.core.api.Assertions.*;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.domain.Reply;
import codesquad.codestagram.domain.User;
import codesquad.codestagram.repository.ArticleRepository;
import codesquad.codestagram.repository.ReplyRepository;
import codesquad.codestagram.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ReplyIntegrationTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    private ReplyService replyService;

    SoftAssertions softly = new SoftAssertions();

    @Test
    @DisplayName("댓글 작성시 작성자와 게시글을 함께 저장해야한다.")
    void saveReplyTest() {
        //given
        User user = new User("testUser", "password123", "test", "test@example.com");
        Article article = new Article("test", "testContent", user);

        User savedUser = userRepository.save(user);
        Article savedArticle = articleRepository.save(article);

        //when
        replyService.addReply("content", article, user);
        Reply reply = replyRepository.findAll().getFirst();
        //then
        softly.assertThat(reply.getUser()).isEqualTo(savedUser);
        softly.assertThat(reply.getArticle()).isEqualTo(savedArticle);
    }

    @Test
    @DisplayName("삭제되지 않은 댓글만 가져올 수 있다.")
    void findReplyTest(){
        //given
        User user = new User("testUser", "password123", "test", "test@example.com");
        Article article = new Article("test", "testContent", user);
        Reply reply1= new Reply("content", article, user);
        Reply reply2 = new Reply("content", article, user);

        User savedUser = userRepository.save(user);
        Article savedArticle = articleRepository.save(article);
        replyRepository.save(reply1);
        replyRepository.save(reply2);

        //when
        replyService.deleteReply(reply2);
        Reply foundreply = replyService.findReplyByIdAndNotDeleted(1L);

        //then

        softly.assertThatThrownBy(()-> replyService.findReplyByIdAndNotDeleted(2L))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("댓글을 찾을 수 없습니다.");

        softly.assertThat(foundreply.getContent()).isEqualTo("content");
        softly.assertThat(foundreply.getUser()).isEqualTo(savedUser);
        softly.assertThat(foundreply.getArticle()).isEqualTo(savedArticle);
    }


}
