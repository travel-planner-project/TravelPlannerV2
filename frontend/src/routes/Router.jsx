import { RouterProvider, createBrowserRouter } from "react-router-dom";
import PlannerLayout from "../layout/PlannerLayout";
import FeedsPage from "../pages/FeedsPage";
import TodosPage from "../pages/TodosPage";

const router = createBrowserRouter([
  {
    path: "/planner",
    element: <PlannerLayout />,
    children: [
      {
        index: true,
        element: <FeedsPage />,
      },

      {
        path: "todo",
        element: <TodosPage />,
      },
    ],
  },
]);

export default function Router() {
  return <RouterProvider router={router} />;
}
