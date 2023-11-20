import styled from "styled-components";
import { ReactComponent as Plus } from "../../assets/plus-solid.svg";
import useInput from "./useInput";
import { useRecoilState } from "recoil";
import { todosAtom } from "../../provider/recoil/atom";

export default function TodoForm({ label }) {
  const [todos, setTodos] = useRecoilState(todosAtom);
  const [input, onChange, refresh] = useInput("");

  const onSubmit = (e) => {
    e.preventDefault();

    const newInput = {
      id: todos.length + 1,
      content: input,
      label,
      complete: false,
    };

    setTodos(todos.concat(newInput));

    refresh();
  };

  return (
    <StTodoForm onSubmit={onSubmit}>
      <TodoFormButton />
      <TodoFormInput
        value={input}
        onChange={onChange}
        placeholder="내용을 입력해주세요"
      />
    </StTodoForm>
  );
}

const StTodoForm = styled.form`
  padding: 12px 0;
  margin: 12px 0;
  box-sizing: border-box;
  border-bottom: 1px solid #e3e8ef;
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  gap: 8px;
`;

const TodoFormButton = styled(Plus)`
  width: 0.95rem;
  height: 0.95rem;
  fill: rgb(50, 129, 252, 0.5);
`;

const TodoFormInput = styled.input`
  width: 250px;
  border: none;
  outline: none;
  font-size: 0.875rem;
  font-weight: 400;
  color: #666;

  &::placeholder {
    color: rgb(50, 129, 252, 0.5);
  }
`;
