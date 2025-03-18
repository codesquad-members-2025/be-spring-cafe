package codesquad.codestagram.service;

import codesquad.codestagram.domain.Board;
import codesquad.codestagram.dto.BoardRequestDto;
import codesquad.codestagram.dto.BoardResponseDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class BoardService {
    private Map<Long, Board> boardMap = new HashMap<>();
    private Long boardSeq = 1L;

    public BoardResponseDto createBoard(BoardRequestDto dto, String writer) {
        Board board = new Board(boardSeq, dto.getTitle(), dto.getContent(), writer);
        boardMap.put(boardSeq, board);
        boardSeq++;
        return new BoardResponseDto(board.getId(), board.getTitle(), board.getContent(), board.getWriter(), board.getUploadDate());
    }

    public ArrayList<BoardResponseDto> getAllPosts() {
        ArrayList<BoardResponseDto> boards = new ArrayList<>();
        for (Board board : boardMap.values()) {
            BoardResponseDto dto = new BoardResponseDto(board.getId(), board.getTitle(), board.getContent(), board.getWriter(), board.getUploadDate());
            boards.add(dto);
        }
        return boards;
    }

    public BoardResponseDto getUserById(Long id) {
        for (Board board : boardMap.values()) {
            if (board.getId() == id) {
                return new BoardResponseDto(board.getId(), board.getTitle(), board.getContent(), board.getWriter(), board.getUploadDate());
            }
        }
        return null;
    }
}
