package com.travelplanner.v2.domain.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.travelplanner.v2.domain.planner.plan.planner.domain.Planner;
import com.travelplanner.v2.domain.post.post.Post;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor (access = AccessLevel.PROTECTED)
@Getter
@Builder
@AllArgsConstructor
public class User implements Serializable { // 레디스에 유저정보를 캐싱을 위해 구현 하였습니다.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userNickname;

    @Email
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String provider;

    private String profileImageUrl;


    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    @Builder.Default
    private List<Planner> planners = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade=CascadeType.REMOVE)
    @Builder.Default
    private List<Post> posts = new ArrayList<>();


    // 연관관계 편의 메서드
    public void mappingPlanner(Planner planner) {
        planners.add(planner);
    }

    public void edit(UserEditor userEditor) {
        if (userEditor.getUserNickname() != null) {
            userNickname = userEditor.getUserNickname();
        }
        if (userEditor.getPassword() != null) {
            password = userEditor.getPassword(); // 비밀번호 수정 로직 추가
        }
        if (userEditor.getProfileImageUrl() != null) {
            profileImageUrl = userEditor.getProfileImageUrl(); // 비밀번호 수정 로직 추가
        }
    }
}
