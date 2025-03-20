package codesquad.codestagram.service;


import codesquad.codestagram.domain.Board;
import codesquad.codestagram.repository.BoardRepository;

import java.util.List;
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

    public List<Board> getAllBoards() {
        return boardRepository.findAll();
    }

   //이게 필요할까..
    public Optional<Board> getBoardId(Long boardId){
        return boardRepository.findByBoardId(boardId);
    }


}

