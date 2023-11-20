import styled from "styled-components";
import TodoList from "./TodoList";
import TodoForm from "./TodoForm";
import TodoHead from "./TodoHead";
import { useRecoilValue } from "recoil";
import { caroselAtom } from "../../provider/recoil/atom";
import TodoProgressBar from "./TodoProgressBar";

const days = ["SUN", "MON", "THU", "WED", "THR", "FRI", "SAT"];

export default function TodoTemplate() {
  const page = useRecoilValue(caroselAtom);

  return (
    <StTodoTemplate>
      {days.map((day, i) => (
        <TodoListBox key={i} page={page}>
          <TodoHead label={day} />
          <TodoProgressBar label={day} />
          <TodoForm label={day} />
          <TodoList label={day} />
        </TodoListBox>
      ))}
    </StTodoTemplate>
  );
}

const StTodoTemplate = styled.div`
  width: 1024px;
  height: auto;
  display: flex;
  justify-content: center;
  gap: 72px;
  overflow-x: hidden;
`;

const TodoListBox = styled.div`
  width: 300px;
  transform: translateX(${(props) => props.page * 700}px);
  transition: all 0.5s ease-in-out;
`;
