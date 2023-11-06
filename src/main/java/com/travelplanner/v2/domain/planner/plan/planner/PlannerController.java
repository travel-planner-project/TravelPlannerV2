package com.travelplanner.v2.domain.planner.plan.planner;

import com.travelplanner.v2.domain.planner.plan.planner.dto.request.PlannerCreateRequest;
import com.travelplanner.v2.domain.planner.plan.planner.dto.request.PlannerDeleteRequest;
import com.travelplanner.v2.domain.planner.plan.planner.dto.request.PlannerUpdateRequest;
import com.travelplanner.v2.domain.planner.plan.planner.dto.response.PlannerCreateResponse;
import com.travelplanner.v2.domain.planner.plan.planner.dto.response.PlannerDetailResponse;
import com.travelplanner.v2.domain.planner.plan.planner.dto.response.PlannerListResponse;
import com.travelplanner.v2.global.exception.ApiException;
import com.travelplanner.v2.global.exception.ApiExceptionResponse;
import com.travelplanner.v2.global.exception.ErrorType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Planner", description = "플래너 API")
@Slf4j
@RestController
@RequestMapping("/api/planners")
@AllArgsConstructor
public class PlannerController {
    private final PlannerService plannerService;

    @Operation(summary = "플래너 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "플래너 리스트 조회 성공"),
            @ApiResponse(responseCode = "403", description = "권한이 부족하여 접근할 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ApiExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "페이지를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ApiExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "유저를 찾을 수 없습니.",
                    content = @Content(schema = @Schema(implementation = ApiExceptionResponse.class)))
    })
    @GetMapping(value = "")
    public Page<PlannerListResponse> getPlannerList(
            Pageable pageable,
            @Parameter(name = "userId", description = "유저 인덱스", in = ParameterIn.QUERY) // swagger
            @RequestParam(required = false) Long userId, HttpServletRequest request) {

        return plannerService.getPlannerListByUserIdOrEmail(pageable, userId, request);
    }

    @Operation(summary = "플래너 상세 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "플래너 리스트 조회 성공"),
            @ApiResponse(responseCode = "403", description = "권한이 부족하여 접근할 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ApiExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "페이지를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ApiExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "유저를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ApiExceptionResponse.class)))
    })
    @GetMapping("/{plannerId}")
    public ResponseEntity<PlannerDetailResponse> getPlannerDetail(@PathVariable Long plannerId) {
        PlannerDetailResponse plannerDetailResponse = plannerService.getPlannerDetailByOrderAndEmail(plannerId);
        return ResponseEntity.ok(plannerDetailResponse);
    }

    @Operation(summary = "플래너 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "플래너 삭제 성공"),
            @ApiResponse(responseCode = "403", description = "권한이 부족하여 접근할 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ApiExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "페이지를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ApiExceptionResponse.class)))
    })
    @DeleteMapping
    //플래너 삭제
    public void deletePlanner(@RequestBody PlannerDeleteRequest plannerDeleteRequest) {
        plannerService.deletePlanner(plannerDeleteRequest);
    }

    @Operation(summary = "플래너 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "플래너 생성 성공"),
            @ApiResponse(responseCode = "500", description = "입력하지 않은 요소가 존재합니다.",
                    content = @Content(schema = @Schema(implementation = ApiExceptionResponse.class)))
    })
    @PostMapping
    public ResponseEntity<PlannerCreateResponse> createPlanner(
            @RequestBody @Validated PlannerCreateRequest plannerCreateRequest, BindingResult result) {
        if (result.hasErrors()) {
            System.out.println("PlannerController.createPlanner");
            throw new ApiException(ErrorType.INVALID_REQUEST);
        }
        PlannerCreateResponse plannerCreateResponse = plannerService.createPlanner(plannerCreateRequest);
        return ResponseEntity.ok(plannerCreateResponse);
    }

    @Operation(summary = "플래너 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "플래너 수정 성공"),
            @ApiResponse(responseCode = "422", description = "페이지를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ApiExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "입력하지  않은 요소가 존재합니다.",
                    content = @Content(schema = @Schema(implementation = ApiExceptionResponse.class)))
    })
    @PatchMapping
    public void updatePlanner(@RequestBody @Validated PlannerUpdateRequest plannerUpdateRequest, BindingResult result) {
        if (result.hasErrors()) {
            throw  new ApiException(ErrorType.INVALID_REQUEST);
        }
        plannerService.updatePlanner (plannerUpdateRequest);
    }
}
