package study.board.api.repo.board;

import org.springframework.data.jpa.repository.JpaRepository;
import study.board.api.entity.board.Board;
import study.board.api.entity.board.Post;

import java.util.List;

public interface PostJapRepo extends JpaRepository<Post, Long> {
    List<Post> findByBoardOrderByIdDesc(Board board);
}
