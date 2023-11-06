package com.travelplanner.v2.domain.post.post.dto.response;

import com.travelplanner.v2.domain.post.image.Image;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(description = "포스트 상세 응답 DTO")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDetailResponse {
    @Schema(description = "포스트 인덱스", example = "1")
    private Long postId;

    @Schema(description = "포스트 제목", example = "포스트1")
    private String postTitle;

    @Schema(description = "포스트 내용", example = "잠을 자고 싶다")
    private String postContent;

    @Schema(description = "포스트 생성일" , example = "9/19")
    private LocalDateTime createdAt;

    @Schema(description = "포스트 이미지 리스트", example = "")
    private List<Image> images;
}

