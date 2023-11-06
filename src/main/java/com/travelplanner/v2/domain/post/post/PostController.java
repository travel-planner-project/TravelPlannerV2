package com.travelplanner.v2.domain.post.post;

import com.travelplanner.v2.domain.post.post.dto.request.PostCreateRequest;
import com.travelplanner.v2.domain.post.post.dto.request.PostDeleteRequest;
import com.travelplanner.v2.domain.post.post.dto.request.PostUpdateRequest;
import com.travelplanner.v2.domain.post.post.dto.response.PostDetailResponse;
import com.travelplanner.v2.domain.post.post.dto.response.PostListResponse;
import com.travelplanner.v2.global.exception.ApiExceptionResponse;
import com.travelplanner.v2.global.util.PageUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Tag(name = "Post", description = "포스트 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    @Operation(summary = "포스트 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "포스트 리스트 조회 성공"),
            @ApiResponse(responseCode = "404", description = "페이지를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ApiExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "유저를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ApiExceptionResponse.class)))
    })
    @GetMapping
    public PageUtil<PostListResponse> getPostList(
            @Parameter(name="page", description = "몇번째 페이지(0부터 시작), 기본값 0", in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "0") int page,

            @Parameter(name = "size", description = "희망 플래너 갯수, 기본값 6", in = ParameterIn.QUERY) // swagger
            @RequestParam(defaultValue = "6") int size,

            HttpServletRequest request) {
        PageUtil<PostListResponse> posts = postService.getPostList(PageRequest.of(page, size), request);
        return posts;
    }

    @Operation(summary = "포스트 상세 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "포스트 상세 조회 성공"),
            @ApiResponse(responseCode = "404", description = "페이지를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ApiExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "유저를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ApiExceptionResponse.class)))
    })
    @GetMapping("/{postId}")
    public ResponseEntity<PostDetailResponse> getPostDetail(@PathVariable Long postId, HttpServletRequest request) {
        PostDetailResponse postDetailResponse = postService.getPostDetailById(postId, request);
        return ResponseEntity.ok().body(postDetailResponse);
    }

    @Operation(summary = "포스트 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "포스트 삭제 성공"),
            @ApiResponse(responseCode = "403", description = "권한이 부족하여 접근할 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ApiExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "페이지를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ApiExceptionResponse.class)))
    })
    @DeleteMapping
    //포스트 삭제
    public ResponseEntity<?> deletePost(HttpServletRequest request, @RequestBody PostDeleteRequest postDeleteRequest) {
        return postService.deletePost(request, postDeleteRequest);
    }

    @Operation(summary = "포스트 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "포스트 생성 성공"),
            @ApiResponse(responseCode = "500", description = "입력 하지 않은 요소가 존재합니다.",
                    content = @Content(schema = @Schema(implementation = ApiExceptionResponse.class)))
    })
    @PostMapping
    public ResponseEntity<?> createPost(
            @RequestPart (value = "multipartFileList", required = false) List<MultipartFile> fileList,
            @RequestPart PostCreateRequest postCreateRequest, HttpServletRequest request) throws IOException {

        return postService.createPost(fileList, postCreateRequest);
    }


    @Operation(summary = "포스트 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "포스트 수정 성공"),
            @ApiResponse(responseCode = "404", description = "페이지를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ApiExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "입력하지 않은 요소가 존재합니다.",
                    content = @Content(schema = @Schema(implementation = ApiExceptionResponse.class)))
    })
    @PatchMapping
    public ResponseEntity<?> updatePost(
            @RequestPart (value = "multipartFileList", required = false) List<MultipartFile> fileList,
            @RequestPart PostUpdateRequest postUpdateRequest) throws IOException {

        return postService.updatePost(fileList, postUpdateRequest);
    }


}

