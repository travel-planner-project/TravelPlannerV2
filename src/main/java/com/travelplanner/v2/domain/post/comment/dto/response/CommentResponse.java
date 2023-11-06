package com.travelplanner.v2.domain.post.comment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {
    private Long postId;
    private Long commentId;
    private String commentContent;
    private Long parentId;
}
