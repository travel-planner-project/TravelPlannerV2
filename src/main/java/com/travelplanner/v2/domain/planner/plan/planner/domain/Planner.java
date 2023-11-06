package com.travelplanner.v2.domain.planner.plan.planner.domain;

import com.travelplanner.v2.domain.planner.groupMember.domain.GroupMember;
import com.travelplanner.v2.domain.planner.plan.calendar.domain.Calendar;
import com.travelplanner.v2.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Planner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    private String planTitle = "제목을 입력해주세요";

    @ColumnDefault("false")
    private Boolean isPrivate;

    private LocalDateTime startDate;

    private LocalDateTime endDate;


    // 연관관계 매핑
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "planner",
            cascade = CascadeType.REMOVE,
            fetch = FetchType.EAGER)
    @Builder.Default
    private List<GroupMember> groupMembers = new ArrayList<>();

    @OneToMany(mappedBy = "planner",  cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @Builder.Default
    private List<Calendar> calendars = new ArrayList<>();


    // 날짜 추가
    public void createDate(Calendar calendar){
        this.calendars.add(calendar);
    }

    // 날짜 삭제
    public void deleteDate(Calendar calendar){
        this.calendars.remove(calendar);
    }


    // 연관 관계 편의 메서드
    public void mappingUser(User user) {
        this.user = user;
        user.mappingPlanner(this);
    }

    public void mappingGroupMember(GroupMember groupMember) {
        groupMembers.add(groupMember);
    }
    public void mappingCalendar(Calendar calendar) {
        calendars.add(calendar);
    }


    // editor
    public PlannerEditor.PlannerEditorBuilder toEditor() {
        return PlannerEditor.builder()
                .planTitle(planTitle)
                .isPrivate(isPrivate);
    }

    public void edit(PlannerEditor plannerEditor){
        planTitle = plannerEditor.getPlanTitle();
        isPrivate = plannerEditor.getIsPrivate();
    }
}
