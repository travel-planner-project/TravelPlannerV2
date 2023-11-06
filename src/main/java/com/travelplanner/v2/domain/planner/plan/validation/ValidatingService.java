package com.travelplanner.v2.domain.planner.plan.validation;

import com.travelplanner.v2.domain.planner.groupMember.GroupMemberRepository;
import com.travelplanner.v2.domain.planner.groupMember.domain.GroupMember;
import com.travelplanner.v2.domain.planner.plan.calendar.CalendarRepository;
import com.travelplanner.v2.domain.planner.plan.calendar.domain.Calendar;
import com.travelplanner.v2.domain.planner.plan.planner.PlannerRepository;
import com.travelplanner.v2.domain.planner.plan.planner.domain.Planner;
import com.travelplanner.v2.domain.planner.plan.todo.ToDoRepository;
import com.travelplanner.v2.domain.planner.plan.todo.domain.ToDo;
import com.travelplanner.v2.global.exception.ApiException;
import com.travelplanner.v2.global.exception.ErrorType;
import com.travelplanner.v2.global.util.AuthUtil;
import com.travelplanner.v2.global.webSocket.WebSocketErrorController;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ValidatingService {

    private final PlannerRepository plannerRepository;
    private final CalendarRepository calendarRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final ToDoRepository toDoRepository;
    private final AuthUtil authUtil;
    private final WebSocketErrorController webSocketErrorController;

    // 플래너와 사용자에 대한 검증
    public Planner validatePlannerAndUserAccess(Long plannerId) {
        Planner planner = plannerRepository.findById(plannerId)
                .orElseThrow(() -> {
                            webSocketErrorController.handleChatMessage(ErrorType.PLANNER_NOT_FOUND);
                            return new ApiException(ErrorType.PLANNER_NOT_FOUND);
                        }
                );

        Long userId = authUtil.getLoginUserIndex();
        List<GroupMember> groupMembers =
                groupMemberRepository.findGroupMemberByPlannerId(plannerId);

        if (groupMembers.stream().noneMatch(gm -> gm.getUserId().equals(userId))) {
            webSocketErrorController.handleChatMessage(ErrorType.USER_NOT_FOUND);
            throw new ApiException(ErrorType.USER_NOT_AUTHORIZED);
        }
        return planner;
    }


    // 캘린더에 대한 검증
    public Calendar validateCalendarAccess(Planner planner, Long updateId) {
        Calendar calendar = calendarRepository.findById(updateId)
                .orElseThrow(() -> {
                            webSocketErrorController.handleChatMessage(ErrorType.DATE_NOT_FOUND);
                            return new ApiException(ErrorType.DATE_NOT_FOUND);
                        }
                );

        if (!planner.getCalendars().contains(calendar)) {
            webSocketErrorController.handleChatMessage(ErrorType.DATE_NOT_AUTHORIZED);
            throw new ApiException(ErrorType.DATE_NOT_AUTHORIZED);
        }
        return calendar;
    }

    // 투두에 대한 검증
    public ToDo validateToDoAccess(Calendar calendar, Long updateId) {
        ToDo toDo = toDoRepository.findById(updateId)
                .orElseThrow(() -> {
                            webSocketErrorController.handleChatMessage(ErrorType.TODO_NOT_FOUND);
                            return new ApiException(ErrorType.TODO_NOT_FOUND);
                        }
                );

        if (!calendar.getScheduleItemList().contains(toDo)) {
            webSocketErrorController.handleChatMessage(ErrorType.TODO_NOT_AUTHORIZED);
            throw new ApiException(ErrorType.TODO_NOT_AUTHORIZED);
        }
        return toDo;
    }
}

