package com.travelplanner.v2.domain.planner.groupMember;


import com.travelplanner.v2.domain.planner.groupMember.domain.GroupMember;
import com.travelplanner.v2.domain.planner.groupMember.domain.GroupMemberType;
import com.travelplanner.v2.domain.planner.groupMember.dto.request.GroupMemberCreateRequest;
import com.travelplanner.v2.domain.planner.groupMember.dto.request.GroupMemberDeleteRequest;
import com.travelplanner.v2.domain.planner.groupMember.dto.response.GroupMemberDeleteResponse;
import com.travelplanner.v2.domain.planner.groupMember.dto.response.GroupMemberResponse;
import com.travelplanner.v2.domain.planner.plan.planner.PlannerRepository;
import com.travelplanner.v2.domain.planner.plan.planner.domain.Planner;
import com.travelplanner.v2.domain.user.UserRepository;
import com.travelplanner.v2.domain.user.domain.User;
import com.travelplanner.v2.global.exception.ApiException;
import com.travelplanner.v2.global.exception.ErrorType;
import com.travelplanner.v2.global.webSocket.WebSocketErrorController;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GroupMemberService {

    private final GroupMemberRepository groupMemberRepository;
    private final UserRepository userRepository;
    private final PlannerRepository plannerRepository;
    private final WebSocketErrorController webSocketErrorController;

    // 그룹 멤버 추가
    @Transactional
    public GroupMemberResponse addGroupMember(GroupMemberCreateRequest request, Long plannerId) {
        // 그룹멤버 찾기
        Optional<User> user = userRepository.findById(request.getUserId());
        // 플래너 아이디에 해당하는 그룹 멤버 리스트 조회
        List<GroupMember> groupMembers = groupMemberRepository.findGroupMemberByPlannerId(plannerId);

        if (groupMembers.stream().noneMatch(gm -> gm.getUserId().equals(user.get().getId()))) {
            // 그룹 멤버에 저장할 플래너 조회
            Planner planner = plannerRepository.findPlannerById(plannerId);

            GroupMember groupMember = GroupMember.builder()
                    .email(user.get().getEmail())
                    .userNickname(user.get().getUserNickname())
                    .userId(user.get().getId())
                    .groupMemberType(GroupMemberType.MEMBER)
                    .profileImageUrl(user.get().getProfileImageUrl())
                    .planner(planner)
                    .build();

            groupMemberRepository.save(groupMember);

            return GroupMemberResponse.builder()
                    .groupMemberId(groupMember.getId())
                    .nickname(groupMember.getUserNickname())
                    .userId(groupMember.getUserId())
                    .profileImageUrl(groupMember.getProfileImageUrl())
                    .role(groupMember.getGroupMemberType())
                    .email(groupMember.getEmail())
                    .build();

        }

        webSocketErrorController.handleChatMessage(ErrorType.GROUP_MEMBER_ALREADY_EXIST);
        throw new ApiException(ErrorType.GROUP_MEMBER_ALREADY_EXIST);
    }

    // 그룹 멤버 삭제
    @Transactional
    public GroupMemberDeleteResponse deleteGroupMember(GroupMemberDeleteRequest request) {
        GroupMember groupMember = groupMemberRepository.findGroupMemberById(request.getGroupMemberId());

        groupMemberRepository.delete(groupMember);

        return GroupMemberDeleteResponse.builder()
                .groupMemberId(request.getGroupMemberId())
                .build();
    }

    // 플래너 조회 시 해당 채팅 내역 조회
    public List<GroupMemberResponse> getGroupMemberList(Long plannerId) {
        List<GroupMember> groupMemberList = groupMemberRepository.findByPlannerId(plannerId);
        List<GroupMemberResponse> groupMemberResponses = new ArrayList<>();

        for (GroupMember groupMember : groupMemberList) {
            GroupMemberResponse groupMemberResponse = GroupMemberResponse.builder()
                    .groupMemberId(groupMember.getId())
                    .nickname(groupMember.getUserNickname())
                    .profileImageUrl(groupMember.getProfileImageUrl())
                    .email(groupMember.getEmail())
                    .role(groupMember.getGroupMemberType())
                    .build();

            groupMemberResponses.add(groupMemberResponse);
        }

        return groupMemberResponses;
    }
}
