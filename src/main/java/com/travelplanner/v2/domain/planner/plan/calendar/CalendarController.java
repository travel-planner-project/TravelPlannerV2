package com.travelplanner.v2.domain.planner.plan.calendar;

import com.travelplanner.v2.domain.planner.plan.calendar.dto.request.CalendarCreateRequest;
import com.travelplanner.v2.domain.planner.plan.calendar.dto.request.CalendarUpdateRequest;
import com.travelplanner.v2.domain.planner.plan.calendar.dto.response.CalendarResponse;
import com.travelplanner.v2.domain.planner.plan.todo.ToDoService;
import com.travelplanner.v2.domain.planner.plan.validation.ValidatingService;
import com.travelplanner.v2.global.util.TokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
public class CalendarController {

    private final TokenUtil tokenUtil;
    private final ToDoService toDoService;
    private final CalendarService calendarService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/create-date/{plannerId}")
    public void createDate(@DestinationVariable Long plannerId, @Header("Authorization") String accessToken, CalendarCreateRequest request) {
        tokenUtil.getAuthenticationFromToken(accessToken);
        simpMessagingTemplate.convertAndSend("/sub/planner-message/" + plannerId,
                Map.of("type","add-date", "msg", calendarService.createDate(plannerId, request)
                )
        );
    }

    @MessageMapping("/update-date/{plannerId}/{dateId}")
    public void updateDate(@DestinationVariable Long plannerId, @DestinationVariable Long dateId,
                           CalendarUpdateRequest request, @Header("Authorization") String accessToken) {

        tokenUtil.getAuthenticationFromToken(accessToken);
        simpMessagingTemplate.convertAndSend("/sub/planner-message/" + plannerId,
                Map.of("type","modify-date", "msg", calendarService.updateDate(plannerId, dateId, request, accessToken)
                )
        );
    }

    @MessageMapping("/delete-date/{plannerId}/{dateId}")
    public void deleteDate(@DestinationVariable Long plannerId, @DestinationVariable Long dateId,
                           @Header("Authorization") String accessToken) {

        tokenUtil.getAuthenticationFromToken(accessToken);
        calendarService.deleteDate(plannerId, dateId);

        List<CalendarResponse> calendarScheduleList = toDoService.getCalendarScheduleList(plannerId);
        simpMessagingTemplate.convertAndSend("/sub/planner-message/" + plannerId,
                Map.of("type","delete-schedule", "msg", calendarScheduleList
                )
        );
    }
}

