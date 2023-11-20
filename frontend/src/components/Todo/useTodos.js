import { useRecoilValue } from "recoil";
import { todosAtom } from "../../provider/recoil/atom";

export default function useTodos({ label }) {
  const todos = useRecoilValue(todosAtom);

  const newTodos = todos.filter((todo) => todo.label === label);

  return newTodos;
}
