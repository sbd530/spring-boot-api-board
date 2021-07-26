package study.board.api.entity.board;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import study.board.api.entity.User;
import study.board.api.entity.common.CommonDateEntity;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Post extends CommonDateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String author;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(length = 500)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "msrl")
    private User user;

    // Join 테이블이 json 결과에 표시되지 않도록 처리.
    @JsonIgnore
    public Board getBoard() {
        return board;
    }

    //Constructor
    public Post(String author, String title, String content, Board board, User user) {
        this.author = author;
        this.title = title;
        this.content = content;
        this.board = board;
        this.user = user;
    }

    // Update method
    public Post setUpdate(String author, String title, String content) {
        this.author = author;
        this.title = title;
        this.content = content;
        return this;
    }
}
