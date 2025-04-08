package codesquad.codestagram.exception;

public class BoardNotFoundException extends RuntimeException {

    public BoardNotFoundException() {
        super("요청하신 게시글을 찾을 수 없습니다.");
    }

//    public BoardNotFoundException(String message) {
//        super(message);
//    }
}