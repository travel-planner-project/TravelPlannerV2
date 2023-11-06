package com.travelplanner.v2.domain.planner.groupMember.domain;

import com.travelplanner.v2.domain.planner.plan.planner.domain.Planner;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class GroupMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 이메일
    private String email;

    // 닉네임
    private String userNickname;

    // 유저 인덱스
    private Long userId;

    // 멤버 유형
    @Enumerated(EnumType.STRING)
    private GroupMemberType groupMemberType;

    // 프로필 이미지
    private String profileImageUrl;

    // 연관관계 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "planner_id")
    private Planner planner;


    // 연관관계 편의 메서드
    public void mappingPlanner(Planner planner) {
        this.planner = planner;
        planner.mappingGroupMember(this);
    }
}

