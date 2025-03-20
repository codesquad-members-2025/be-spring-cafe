package codesquad.codestagram.service;

import codesquad.codestagram.Entity.Board;
import codesquad.codestagram.Entity.User;
import codesquad.codestagram.dto.BoardRequestDto;
import codesquad.codestagram.dto.BoardResponseDto;
import codesquad.codestagram.repository.BoardMapRepository;
import codesquad.codestagram.repository.BoardRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BoardService {
    private BoardRepository boardRepository;
    private BoardMapRepository boardMapRepository;

    BoardService(@Qualifier("boardJpaRepository") BoardRepository boardRepository,
                 @Qualifier("boardMapRepository") BoardMapRepository boardMapRepository) {
        this.boardRepository = boardRepository;
        this.boardMapRepository = boardMapRepository;
    }

    @Transactional
    public BoardResponseDto createBoard(BoardRequestDto dto, User user) {
        Board board = new Board(dto.getTitle(), dto.getContent(), user);
        board = boardRepository.save(board);
        boardMapRepository.save(board);
        return new BoardResponseDto(board);
    }

    public ArrayList<BoardResponseDto> getAllPosts() {
        List<Board> boards = boardRepository.findAll();
        ArrayList<BoardResponseDto> boardResponseDtos = new ArrayList<>();
        for (Board board : boards) {
            boardResponseDtos.add(new BoardResponseDto(board));
        }
        return boardResponseDtos;
    }

    public BoardResponseDto getBoardById(Long id) {
        Board board = boardRepository.findById(id).orElse(null);
        return (board != null) ? new BoardResponseDto(board) : null;
    }
}
