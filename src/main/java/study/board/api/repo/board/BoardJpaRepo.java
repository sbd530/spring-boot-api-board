package study.board.api.repo.board;

import org.springframework.data.jpa.repository.JpaRepository;
import study.board.api.entity.board.Board;

public interface BoardJpaRepo extends JpaRepository<Board, Long> {
    Board findByName(String name);
}
