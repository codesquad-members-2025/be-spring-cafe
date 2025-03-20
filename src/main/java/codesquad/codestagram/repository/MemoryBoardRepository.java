package codesquad.codestagram.repository;

import codesquad.codestagram.domain.Board;

import java.util.*;
import java.util.Optional;

public class MemoryBoardRepository implements BoardRepository {
    private static Map<Long, Board> store = new HashMap<>();
    private static long sequence = 0L;

    @Override
    public Board save(Board board) {
        board.setBoardId(++sequence);
        store.put(board.getBoardId(), board);
        return board;
    }

    @Override
    public Optional<Board> findByBoardId(Long boardId) {
        return Optional.ofNullable(store.get(boardId));
    }

    @Override
    public List<Board> findAll() {
        return new ArrayList<>(store.values());
    }

    public void clearStore() {
        store.clear();
    }
}
