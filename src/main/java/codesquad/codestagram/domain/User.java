package codesquad.codestagram.domain;

public class User {

    private long id;
    private String nickname;
    private int passWord;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getPassWord() {
        return passWord;
    }

    public void setPassWord(int passWord) {
        this.passWord = passWord;
    }
}
