package study.board.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.board.api.advice.exception.CNotOwnerException;
import study.board.api.advice.exception.CResourceNotExistException;
import study.board.api.advice.exception.CUserNotFoundException;
import study.board.api.entity.User;
import study.board.api.entity.board.Board;
import study.board.api.entity.board.Post;
import study.board.api.model.ParamPost;
import study.board.api.repo.UserJpaRepo;
import study.board.api.repo.board.BoardJpaRepo;
import study.board.api.repo.board.PostJapRepo;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BoardService {
    private final BoardJpaRepo boardJpaRepo;
    private final PostJapRepo postJapRepo;
    private final UserJpaRepo userJpaRepo;

    //게시판 생성
    public Board insertBoard(String boardName) {
        return boardJpaRepo.save(Board.builder().name(boardName).build());
    }

    //게시판 이름으로 게시판 조회
    public Board findBoard(String boardName) {
        return Optional.ofNullable(boardJpaRepo.findByName(boardName))
                .orElseThrow(CResourceNotExistException::new);
    }

    //게시판 이름으로 게시글 리스트 조회
    public List<Post> findPosts(String boardName) {
        return postJapRepo.findByBoardOrderByIdDesc(findBoard(boardName));
    }

    //게시글 id 로 게시글 조회
    public Post getPost(Long postId) {
        return postJapRepo.findById(postId)
                .orElseThrow(CResourceNotExistException::new);
    }

    //게시글 생성
    public Post writePost(String uid, String boardName, ParamPost paramPost) {
        Board board = findBoard(boardName);
        Post post = Post.builder()
                .user(userJpaRepo.findByUid(uid).orElseThrow(CUserNotFoundException::new))
                .board(board)
                .author(paramPost.getAuthor())
                .title(paramPost.getTitle())
                .content(paramPost.getContent())
                .build();
        return postJapRepo.save(post);
    }

    //게시글 수정
    public Post updatePost(Long postId, String uid, ParamPost paramPost) {
        Post targetPost = getPost(postId);
        User owner = targetPost.getUser();
        validateUser(uid, owner);

        targetPost.setUpdate(paramPost.getAuthor(), paramPost.getTitle(), paramPost.getContent());
        return targetPost;
    }

    //게시글 삭제
    public boolean deletePost(Long postId, String uid) {
        Post targetPost = getPost(postId);
        User owner = targetPost.getUser();
        validateUser(uid, owner);

        postJapRepo.delete(targetPost);
        return true;
    }

    private void validateUser(String uid, User owner) {
        if (!uid.equals(owner.getUid()))
            throw new CNotOwnerException();
    }
}
