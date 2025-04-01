package codesquad.codestagram.service;


import codesquad.codestagram.domain.Board;
import codesquad.codestagram.repository.BoardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BoardService {
    private BoardRepository boardRepository;

    //의존성 주입(DI)
    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    /**
     * 게시글 작성
     */
    @Transactional
    public Board writeBoard(Board board) {
        return boardRepository.save(board);
    }

    public List<Board> getAllBoards() {
        return boardRepository.findAll();
    }


    public Optional<Board> getBoardById(Long boardId){
        return boardRepository.findById(boardId);
    }


}

