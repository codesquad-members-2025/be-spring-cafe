package codesquad.codestagram.service;


import codesquad.codestagram.domain.Board;
import codesquad.codestagram.repository.BoardRepository;

import java.util.Optional;

public class BoardService {
    private BoardRepository boardRepository;

    //의존성 주입(DI)
    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    /**
     * 게시글 작성
     */
    public Board writeBoard(Board board) {
        return boardRepository.save(board);
    }

    /**
     * 전체 게시글 조회, 이게 필요한지는 모르겠음.
     */
    public Optional<Board> getBoardId(Long boardId){
        return boardRepository.findByBoardId(boardId);
    }
}

