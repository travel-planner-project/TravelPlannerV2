import { Outlet } from "react-router-dom";
import styled from "styled-components";
import SideBar from "../components/@common/SiderBar";

export default function PlannerLayout() {
  return (
    <StPlannerLayout>
      <SideBar />
      <Outlet />
    </StPlannerLayout>
  );
}

const StPlannerLayout = styled.div`
  position: relative;
  left: 50%;
  width: 1440px;
  height: 100%;
  transform: translateX(-50%);
`;
