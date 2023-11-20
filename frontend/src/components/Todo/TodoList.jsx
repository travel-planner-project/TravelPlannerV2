import styled from "styled-components";
import TodoItem from "./TodoItem";
import useDragDrop from "./useDragDrop";
import useTodos from "./useTodos";

export default function TodoList({ label }) {
  const newTodos = useTodos({ label });
  const [onDragStart, onDragEnter, onDragLeave, onDragEnd] = useDragDrop();

  return (
    <StTodoList>
      {newTodos.map((todo) => (
        <TodoItem
          key={todo.id}
          todo={todo.content}
          onDragStart={(e) => onDragStart(e, todo)}
          onDragEnter={(e) => onDragEnter(e, todo)}
          onDragLeave={onDragLeave}
          onDragEnd={onDragEnd}
        />
      ))}
    </StTodoList>
  );
}

const StTodoList = styled.div`
  width: 100%;
  max-height: 380px;
  overflow-y: scroll;
`;
