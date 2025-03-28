package codesquad.codestagram.service;

import codesquad.codestagram.domain.User;
import codesquad.codestagram.dto.UserForm;
import codesquad.codestagram.exception.DuplicateUserIdException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
public class UserServiceIntegrationTest {
    @Autowired
    private UserService userService;

    @Test
    @DisplayName("join해서 repository에 추가하면 유저의 수가 늘어난다.")
    public void find_user() {
        int userCount = userService.findAllUsers().size();
        UserForm userForm = new UserForm("dino","userName","1234","jd@naver","");
        User user = userService.join(userForm);

        assertThat(userService.findAllUsers().size()).isEqualTo(userCount + 1);
    }

    @Test
    @DisplayName("만약 동일한 아이디가 이미 존재하면, 에러를 던진다.")
    public void can_not_make_same_user() {
        UserForm userForm = new UserForm("dino","userName","1234","jd@naver","");
        User user = userService.join(userForm);

        assertThatThrownBy(()->userService.join(userForm)).isInstanceOf(DuplicateUserIdException.class).hasMessage("ID가 dino인 사용자가 이미 존재합니다.");
    }

    @Test
    @DisplayName("비밀번호를 받아 같으면 유저의 정보를 업데이트 할 수 있다.")
    public void update_user() {
        UserForm userForm = new UserForm("dino","userName","1234","jd@naver","");
        userForm.setUserId("dino");
        userForm.setName("userName");
        userForm.setPassword("password");
        User user = userService.join(userForm);

        UserForm updatedUserForm =  new UserForm("dino","userName2","1234","jd@naver","12345");
        updatedUserForm.setUserId("dino");
        updatedUserForm.setName("userName2");
        updatedUserForm.setPassword("password");
        updatedUserForm.setChangedPassword("changed");
        assertThat(userService.updateUser(user, updatedUserForm)).isEqualTo(true);
        assertThat(userService.findByUserId("dino").getName()).isEqualTo("userName2");
    }

    @Test
    @DisplayName("비밀번호를 받아 다르면 업데이트 하지 않는다.")
    public void do_not_update_user() {
        UserForm userForm = new UserForm("dino","userName","1234","jd@naver","");
        User user = userService.join(userForm);

        UserForm updatedUserForm =  new UserForm("dino","userName2","wrongPassword","jd@naver","12345");
        assertThat(userService.updateUser(user, updatedUserForm)).isEqualTo(false);
        assertThat(userService.findByUserId("dino").getName()).isEqualTo("userName");
    }
}
