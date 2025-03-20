package codesquad.codestagram.dto;

import codesquad.codestagram.domain.User;

public class UserDto {
    public static class UserRequestDto{
        private String userId;
        private String password;
        private String name;
        private String email;

        public UserRequestDto(String userId, String password, String name, String email) {
            this.userId = userId;
            this.password = password;
            this.name = name;
            this.email = email;
        }
        public User toUser(){
            return new User(userId, password, name, email);
        }

        public String getUserId() {
            return userId;
        }
    }
}
