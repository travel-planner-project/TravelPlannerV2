package com.travelplanner.v2.domain.planner.plan.todo;

import com.travelplanner.v2.domain.planner.plan.calendar.dto.response.CalendarResponse;
import com.travelplanner.v2.domain.planner.plan.todo.dto.request.ToDoCreateRequest;
import com.travelplanner.v2.domain.planner.plan.todo.dto.request.ToDoUpdateRequest;
import com.travelplanner.v2.global.util.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ToDoController {
    private final TokenUtil tokenUtil;
    private final ToDoService toDoService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/create-todo/{plannerId}/{dateId}")
    public void create(@DestinationVariable Long plannerId, @DestinationVariable Long dateId,
                       ToDoCreateRequest request, @Header("Authorization") String accessToken) {

        tokenUtil.getAuthenticationFromToken(accessToken);
        simpMessagingTemplate.convertAndSend("/sub/planner-message/" + plannerId,
                Map.of("type","add-schedule", "msg", toDoService.createTodo(plannerId, dateId, request) // 사용자가 입력한 todo
                )
        );
    }

    @MessageMapping("/update-todo/{plannerId}/{dateId}/{toDoId}")
    public void edit(@DestinationVariable Long plannerId, @DestinationVariable Long dateId, @DestinationVariable Long toDoId,
                     ToDoUpdateRequest request, @Header("Authorization") String accessToken) {

        tokenUtil.getAuthenticationFromToken(accessToken);
        simpMessagingTemplate.convertAndSend("/sub/planner-message/" + plannerId,
                Map.of("type","modify-schedule", "msg", toDoService.editTodo(plannerId, dateId, toDoId, request)
                )
        );
    }

    @MessageMapping("/delete-todo/{plannerId}/{dateId}/{toDoId}")
    public void delete(@DestinationVariable Long plannerId, @DestinationVariable Long dateId,
                       @DestinationVariable Long toDoId, @Header("Authorization") String accessToken) {

        tokenUtil.getAuthenticationFromToken(accessToken);
        toDoService.delete(plannerId, dateId, toDoId);
        List<CalendarResponse> calendarScheduleList = toDoService.getCalendarScheduleList(plannerId);
        simpMessagingTemplate.convertAndSend("/sub/planner-message/" + plannerId,
                Map.of("type","delete-schedule", "msg", calendarScheduleList
                )
        );
    }
}
