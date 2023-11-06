package com.travelplanner.v2.domain.post.comment;

import com.travelplanner.v2.domain.post.comment.domain.Comment;
import com.travelplanner.v2.domain.post.comment.domain.CommentEditor;
import com.travelplanner.v2.domain.post.comment.dto.request.CommentCreateRequest;
import com.travelplanner.v2.domain.post.comment.dto.request.CommentUpdateRequest;
import com.travelplanner.v2.domain.post.comment.dto.response.CommentResponse;
import com.travelplanner.v2.domain.post.post.Post;
import com.travelplanner.v2.domain.post.post.PostRepository;
import com.travelplanner.v2.domain.user.UserRepository;
import com.travelplanner.v2.domain.user.domain.User;
import com.travelplanner.v2.global.exception.ApiException;
import com.travelplanner.v2.global.exception.ErrorType;
import com.travelplanner.v2.global.util.AuthUtil;
import com.travelplanner.v2.global.util.PageUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final AuthUtil authUtil;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public PageUtil<CommentResponse> getCommentList(Long postId, Pageable pageable) {
        validatePost(postId);

        Page<Comment> commentList = commentRepository.findByPostId(postId, pageable);
        List<CommentResponse> commentResponseList = new ArrayList<>();

        for (Comment comment : commentList.getContent()) {
            CommentResponse commentResponse = CommentResponse.builder()
                    .postId(comment.getPost().getId())
                    .commentId(comment.getId())
                    .commentContent(comment.getCommentContent())
                    .build();

            commentResponseList.add(commentResponse);
        }

        return new PageUtil<>(commentResponseList, pageable, commentList.getTotalPages());
    }

    @Transactional
    public CommentResponse createComment(Long postId, CommentCreateRequest commentCreateRequest) {

        boolean parentIsNull = commentCreateRequest.getParentId() == null;
        Post post = validatePost(postId);

        Comment comment = null;

        if (parentIsNull) {
            comment = Comment.builder()
                    .post(post)
                    .commentContent(commentCreateRequest.getCommentContent())
                    .build();
        }

        if (!parentIsNull) {
            Long parentId = commentCreateRequest.getParentId();
            Comment findParent = validateComment(parentId);

            comment = Comment.builder()
                    .post(post)
                    .commentContent(commentCreateRequest.getCommentContent())
                    .parent(findParent)
                    .build();
        }

        commentRepository.save(comment);

        CommentResponse.CommentResponseBuilder commentResponseBuilder = CommentResponse.builder()
                .postId(post.getId())
                .commentId(comment.getId())
                .commentContent(comment.getCommentContent());

        CommentResponse commentResponse = null;
        if(parentIsNull){
            commentResponse = commentResponseBuilder
                    .parentId(null)
                    .build();
        }

        if (!parentIsNull) {
            commentResponse = commentResponseBuilder
                    .parentId(commentCreateRequest.getParentId())
                    .build();
        }

        return commentResponse;
    }

    @Transactional
    public void editComment(Long postId, Long commentId, CommentUpdateRequest commentEditRequest) {
        validatePost(postId);

        Comment comment = validateComment(commentId);
        Optional<User> currentUser = authUtil.getLoginUser();
        validateCommentAccess(comment, currentUser);

        CommentEditor.CommentEditorBuilder editorBuilder = comment.toEditor();
        log.info("flag 1 commentEditRequest.getCommentContent() = " + commentEditRequest.getCommentContent());

        CommentEditor commentEditor = editorBuilder
                .commentContent(commentEditRequest.getCommentContent())
                .build();
        log.info("flag 2 commentEditor.getCommentContent() = " + commentEditor.getCommentContent());

        comment.edit(commentEditor);
        log.info("flag 3 comment.getCommentContent() = " + comment.getCommentContent());
    }

    @Transactional
    public void deleteComment(Long postId, Long commentId, HttpServletRequest request) {

        validatePost(postId);
        Comment comment = validateComment(commentId);

        Optional<User> currentMember = authUtil.getLoginUser();

        validateCommentAccess(comment, currentMember);

        commentRepository.delete(comment);
    }

    // Post 존재 여부 검증
    public Post validatePost(Long postId){
        return postRepository.findById(postId)
                .orElseThrow(() -> new ApiException(ErrorType.POST_NOT_FOUND));
    }

    // Comment 존재 여부 검증
    public Comment validateComment(Long commentId){
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new ApiException(ErrorType.COMMENT_NOT_FOUND));
    }

    // 작성자와 현재 로그인한 유저가 다를 경우
    public void validateCommentAccess(Comment comment, Optional<User> currentUser) {
        if (!comment.getPost().getUser().equals(currentUser)) {
            throw new ApiException(ErrorType.USER_NOT_AUTHORIZED);
        }
    }
}

