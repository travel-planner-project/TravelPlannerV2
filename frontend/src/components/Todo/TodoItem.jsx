import styled from "styled-components";
import { ReactComponent as Arrow } from "../../assets/arrow-right-solid.svg";

export default function TodoItem({
  todo,
  onDragStart,
  onDragEnter,
  onDragLeave,
  onDragEnd,
}) {
  return (
    <StTodoItem
      draggable="true"
      onDragStart={onDragStart}
      onDragEnter={onDragEnter}
      onDragLeave={onDragLeave}
      onDragEnd={onDragEnd}
    >
      <TodoItemCheckBox />
      <TodoItemContent>{todo}</TodoItemContent>
      <TodoItemArrow />
    </StTodoItem>
  );
}

const StTodoItem = styled.li`
  position: relative;
  width: 100%;
  padding: 14px 12px;
  margin: 24px 0;
  box-sizing: border-box;
  border-radius: 4px;
  border: 2px solid #e8e8e8;
  background: #ffffff;
  display: flex;
  justify-content: space-between;
  align-items: center;
`;

const TodoItemCheckBox = styled.input.attrs({ type: "checkbox" })`
  appearance: none;
  cursor: pointer;
  position: absolute;
  top: 50%;
  left: 14px;
  width: 0.95rem;
  height: 0.95rem;
  padding: 0;
  margin: 0;
  border: 2px solid #aaa;
  transform: translateY(-50%);

  &:checked {
    border-color: transparent;
    background-image: url("data:image/svg+xml,%3csvg viewBox='0 0 16 16' fill='white' xmlns='http://www.w3.org/2000/svg'%3e%3cpath d='M5.707 7.293a1 1 0 0 0-1.414 1.414l2 2a1 1 0 0 0 1.414 0l4-4a1 1 0 0 0-1.414-1.414L7 8.586 5.707 7.293z'/%3e%3c/svg%3e");
    background-size: 100% 100%;
    background-position: 50%;
    background-repeat: no-repeat;
    background-color: #6588f7;
  }
`;

const TodoItemContent = styled.h5`
  width: 150px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  margin-top: 2px;
  margin-left: 36px;
  font-size: 0.95rem;
  font-weight: bold;
  color: #666;
`;

const TodoItemArrow = styled(Arrow)`
  width: 0.95rem;
  height: 0.95rem;
  fill: #888;
`;
