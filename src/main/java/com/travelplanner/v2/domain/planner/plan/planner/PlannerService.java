package com.travelplanner.v2.domain.planner.plan.planner;

import com.travelplanner.v2.domain.planner.chat.ChatService;
import com.travelplanner.v2.domain.planner.groupMember.GroupMemberRepository;
import com.travelplanner.v2.domain.planner.groupMember.GroupMemberService;
import com.travelplanner.v2.domain.planner.groupMember.domain.GroupMember;
import com.travelplanner.v2.domain.planner.groupMember.domain.GroupMemberType;
import com.travelplanner.v2.domain.planner.groupMember.dto.response.GroupMemberResponse;
import com.travelplanner.v2.domain.planner.plan.calendar.CalendarService;
import com.travelplanner.v2.domain.planner.plan.calendar.dto.response.CalendarResponse;
import com.travelplanner.v2.domain.planner.plan.planner.domain.Planner;
import com.travelplanner.v2.domain.planner.plan.planner.domain.PlannerEditor;
import com.travelplanner.v2.domain.planner.plan.planner.dto.request.PlannerCreateRequest;
import com.travelplanner.v2.domain.planner.plan.planner.dto.request.PlannerDeleteRequest;
import com.travelplanner.v2.domain.planner.plan.planner.dto.request.PlannerUpdateRequest;
import com.travelplanner.v2.domain.planner.plan.planner.dto.response.PlannerCreateResponse;
import com.travelplanner.v2.domain.planner.plan.planner.dto.response.PlannerDetailAuthorizedResponse;
import com.travelplanner.v2.domain.planner.plan.planner.dto.response.PlannerDetailResponse;
import com.travelplanner.v2.domain.planner.plan.planner.dto.response.PlannerDetailUnauthorizedResponse;
import com.travelplanner.v2.domain.planner.plan.planner.dto.response.PlannerListResponse;
import com.travelplanner.v2.domain.planner.plan.todo.ToDoService;
import com.travelplanner.v2.domain.planner.plan.todo.dto.ToDoResponse;
import com.travelplanner.v2.domain.planner.plan.validation.ValidatingService;
import com.travelplanner.v2.domain.user.UserRepository;
import com.travelplanner.v2.domain.user.domain.User;
import com.travelplanner.v2.global.exception.ApiException;
import com.travelplanner.v2.global.exception.ErrorType;
import com.travelplanner.v2.global.util.AuthUtil;
import com.travelplanner.v2.global.util.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PlannerService {
    private final UserRepository userRepository;
    private final PlannerRepository plannerRepository;
    private final GroupMemberRepository groupMemberRepository;

    // planner detail조회에서 캘린더 목록 및 투두 목록을 갖고오기 위해 추가
    private final CalendarService calendarService;
    private final ToDoService toDoService;
    private final ValidatingService validatingService;
    private final ChatService chatService;
    private final GroupMemberService groupMemberService;

    private final AuthUtil authUtil;

    // 플래너 리스트
    // TODO page 처리 혼동 해결하기
    public Page<PlannerListResponse> getPlannerListByUserIdOrEmail(Pageable pageable, Long userId, HttpServletRequest request) {
        List<Planner> planners;

        if (userId == null) {
            // 유저 아이디 값을 보내지 않았을 때 예외 던지기
            throw new ApiException(ErrorType.NULL_VALUE_EXIST);

        } else {
            // 특정 사용자의 플래너 조회
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ApiException(ErrorType.USER_NOT_FOUND));
            planners = plannerRepository.findByUserOrderByIdDesc(user);
        }

        // 로그인한 경우여서 인증을 받을 수 있는 경우에는 아래와 같이 진행
        Long currentUserId = authUtil.getLoginUserIndex();

        if (currentUserId != null) {
            log.info("====================================================");
            log.info("userId: " + currentUserId);
            log.info("====================================================");

            // 현재 사용자가 그룹 멤버인지 확인
            List<GroupMember> groupMembers = groupMemberRepository.findByUserId(currentUserId);

            // 로그인한 사용자가 속한 모든 그룹의 플래너를 찾음
            for(GroupMember gm : groupMembers) {
                Planner planner = gm.getPlanner();
                // 이 플래너가 이미 사용자의 플래너 리스트에 있는지 확인 후 없다면 추가 (중복 제거)
                if(!planners.contains(planner)) {
                    planners.add(planner);
                }
            }

            // 현재 사용자가 그룹 멤버가 아닌 경우 isPrivate이 true인 플래너를 제거
            planners = planners.stream()
                    .filter(planner -> !planner.getIsPrivate() || groupMembers.stream().anyMatch(gm -> gm.getPlanner().equals(planner)))
                    .collect(Collectors.toList());

        } else { // 비로그인한 유저의 경우 공개 플래너만 모아서 반환

            // isPrivate = true 인 플래너 제거
            planners = planners.stream()
                    .filter(planner -> !planner.getIsPrivate())
                    .collect(Collectors.toList());
        }

        // ==================================================

        // [공통] 페이지 처리 ======================================

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), planners.size());
        Page<Planner> plannerPage = new PageImpl<>(planners.subList(start, end), pageable, planners.size());

        return plannerPage.map(planner -> {
            List<GroupMemberResponse> groupMemberResponses = groupMemberService.getGroupMemberList(planner.getId());
            return new PlannerListResponse(planner, groupMemberResponses);  // 수정된 부분
        });
    }

    public PlannerDetailResponse getPlannerDetailByOrderAndEmail(Long plannerId) {
        Planner planner = plannerRepository.findPlannerById(plannerId);
        // Planner에 해당하는 캘린더 리스트를 가져옴
        List<CalendarResponse> calendarResponses = calendarService.getCalendarList(planner.getId());

        // 각 캘린더에 해당하는 투두 리스트를 가져와 CalendarResponse에 추가
        List<CalendarResponse> updatedCalendarResponses = new ArrayList<>();
        for (CalendarResponse calendarResponse : calendarResponses) {
            List<ToDoResponse> toDoResponses = toDoService.getScheduleItemList(calendarResponse.getDateId());
            CalendarResponse updatedCalendarResponse = CalendarResponse.builder()
                    .dateId(calendarResponse.getDateId())
                    .dateTitle(calendarResponse.getDateTitle())
                    .createAt(calendarResponse.getCreateAt())
                    .plannerId(calendarResponse.getPlannerId())
                    .scheduleItemList(toDoResponses)
                    .build();
            updatedCalendarResponses.add(updatedCalendarResponse);
        }

        // 플래너에 해당하는 그룹멤버를 가져옴
        List<GroupMemberResponse> groupMemberResponses = groupMemberService.getGroupMemberList(planner.getId());
        Long userId = authUtil.getLoginUserIndex();

        // 로그인한 경우여서 인증을 받을 수 있는 경우에는 아래와 같이 진행
        if (userId != null) {
            if (planner.getIsPrivate()) {
                planner = validatingService.validatePlannerAndUserAccess(plannerId); // 그룹멤버만 볼 수 있도록 하는 메서드
            }

            return PlannerDetailAuthorizedResponse.builder()
                    .plannerId(planner.getId())
                    .planTitle(planner.getPlanTitle())
                    .isPrivate(planner.getIsPrivate())
                    .startDate(planner.getStartDate())
                    .endDate(planner.getEndDate())
                    .calendars(updatedCalendarResponses)
                    .groupMemberList(groupMemberResponses)
                    .build();
        }

        return PlannerDetailUnauthorizedResponse.builder()
                .plannerId(planner.getId())
                .planTitle(planner.getPlanTitle())
                .isPrivate(planner.getIsPrivate())
                .startDate(planner.getStartDate())
                .endDate(planner.getEndDate())
                .calendars(updatedCalendarResponses)
                .groupMemberList(groupMemberResponses)
                .build();
    }

    @Transactional
    //플래너 삭제
    public void deletePlanner(PlannerDeleteRequest plannerDeleteRequest) {
        Long plannerId = plannerDeleteRequest.getPlannerId();
        log.info("Received delete request for plannerId: {}", plannerDeleteRequest.getPlannerId());

        // 조회했을 때 플래너가 존재하지 않을 경우
        Planner planner = plannerRepository.findById(plannerId)
                .orElseThrow(() -> new ApiException(ErrorType.PAGE_NOT_FOUND));

        // 플래너를 생성한 사람일 경우
        // 플래너를 만든사람 == currentUser : 플래너를 아예 삭제한다.
        if (planner.getUser().getId().equals(authUtil.getLoginUserIndex())) {
            // 이때 플래너와 관련된 그룹멤버도 전부 삭제
            groupMemberRepository.deleteAllByPlannerId(plannerId);
            plannerRepository.delete(planner);

        } else {
            // 플래너를 생성한 사람이 아닐 경우
            // 플래너를 만든사람 /= currentUser: 삭제 권한 없음
            throw new ApiException(ErrorType.USER_NOT_AUTHORIZED);
        }
    }

    @Transactional
    public PlannerCreateResponse createPlanner(PlannerCreateRequest plannerCreateRequest) {
        Long currentUserId = authUtil.getLoginUserIndex();
        Optional<User> user = userRepository.findById(currentUserId);

        log.info("request.getPlanTitle() = {}", plannerCreateRequest.getPlanTitle());
        log.info("request.getIsPrivate() = {}", plannerCreateRequest.getIsPrivate());
        log.info("request.getPlanTitle().getClass() = {}", plannerCreateRequest.getPlanTitle().getClass());

        Planner createPlanner = Planner.builder()
                .planTitle(plannerCreateRequest.getPlanTitle())
                .isPrivate(plannerCreateRequest.getIsPrivate())
                .user(user.get())
                .build();

        plannerRepository.save(createPlanner);

        // 플래너 만든 사람은 HOST 로 처음에 들어가 있어야 함
        // 플래너 만든 사람은 현재 로그인 한 사람
        GroupMember groupMember = GroupMember.builder()
                .email(user.get().getEmail())
                .groupMemberType(GroupMemberType.HOST)
                .userNickname(user.get().getUserNickname())
                .userId(user.get().getId())
                .profileImageUrl(user.get().getProfileImageUrl())
                .planner(createPlanner)
                .build();

        groupMemberRepository.save(groupMember);

        PlannerCreateResponse plannerCreateResponse = PlannerCreateResponse.builder()
                .plannerId(createPlanner.getId())
                .planTitle(createPlanner.getPlanTitle())
                .isPrivate(createPlanner.getIsPrivate())
                .build();

        return plannerCreateResponse;
    }

    @Transactional
    public void updatePlanner(PlannerUpdateRequest plannerEditRequest) {

        Long plannerId = plannerEditRequest.getPlannerId();

        // 조회했을 때 플래너가 존재하지 않을 경우
        Planner planner = plannerRepository.findById(plannerId)
                .orElseThrow(() -> new ApiException(ErrorType.PAGE_NOT_FOUND));

        Long currentUserId = authUtil.getLoginUserIndex();
        // 플래너를 생성한 사람이 아닐 경우
        if (!planner.getUser().getId().equals(currentUserId)) {
            throw new ApiException(ErrorType.USER_NOT_AUTHORIZED);
        }

        PlannerEditor.PlannerEditorBuilder editorBuilder = planner.toEditor();
        PlannerEditor plannerEditor = editorBuilder
                .planTitle(plannerEditRequest.getPlanTitle())
                .isPrivate(plannerEditRequest.getIsPrivate())
                .build();
        planner.edit(plannerEditor);
    }
}


