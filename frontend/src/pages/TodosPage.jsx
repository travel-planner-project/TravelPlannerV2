import TodoSection from "../components/Todo/TodoSection";
import TodoProvider from "../provider/recoil/provider";

export default function TodosPage() {
  return (
    <TodoProvider>
      <TodoSection />
    </TodoProvider>
  );
}
