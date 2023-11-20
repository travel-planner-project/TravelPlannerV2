import { useRecoilState } from "recoil";
import { todoAtom, todosAtom } from "../../provider/recoil/atom";

export default function useDragDrop() {
  const [grab, setGrab] = useRecoilState(todoAtom);
  const [todos, setTodos] = useRecoilState(todosAtom);

  const onDragStart = (e, todo) => {
    setGrab({ draggedTodo: todo, target: e.target, updateTodos: null });

    e.dataTransfer.effectAllowed = "move";
  };

  const onDragEnter = (e, targetTodo) => {
    e.preventDefault();

    let { draggedTodo } = grab;
    const updateTodos = [...todos];

    if (draggedTodo.label !== targetTodo.label) {
      draggedTodo = { ...draggedTodo, label: targetTodo.label };
    }

    const draggedIndex = updateTodos.findIndex(
      (todo) => todo.id === draggedTodo.id
    );
    const targetIndex = updateTodos.findIndex(
      (todo) => todo.id === targetTodo.id
    );

    updateTodos.splice(draggedIndex, 1);
    updateTodos.splice(targetIndex, 0, draggedTodo);

    setGrab((prev) => ({ ...prev, updateTodos }));
  };

  const onDragLeave = (e) => {
    e.preventDefault();

    if (e.target === grab.target) {
      e.target.style.visibility = "hidden";
    }
  };

  const onDragEnd = (e) => {
    e.preventDefault();

    setTodos([...grab.updateTodos]);

    e.target.style.visibility = "visible";
  };

  return [onDragStart, onDragEnter, onDragLeave, onDragEnd];
}
