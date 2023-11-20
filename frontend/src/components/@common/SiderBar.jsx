import styled from "styled-components";
import { ReactComponent as Arrow } from "../../assets/arrow-right-solid.svg";
import { ReactComponent as Ellipsis } from "../../assets/ellipsis-solid.svg";
import { ReactComponent as Plus } from "../../assets/plus-solid.svg";

function SideBarUser() {
  return (
    <SideBarUserBox>
      <div className="profile"></div>
      <h3 className="name">김민승</h3>
      <h5 className="desc">여행을 좋아하는 작가입니다.</h5>
    </SideBarUserBox>
  );
}

function SideBarPlanner() {
  return (
    <SideBarPlannerBox>
      <li className="planner-item">
        부산 여행 플래너...! <Arrow />
      </li>
      <li className="planner-item">
        세종 여행 플래너...! <Arrow />
      </li>
      <li className="planner-item">
        서울 여행 플래너...! <Arrow />
      </li>
      <li className="planner-item">
        거제도 여행 플래너...! <Arrow />
      </li>
    </SideBarPlannerBox>
  );
}

function SideBarTodo() {}

export default function SideBar() {
  return (
    <SideBarAside>
      <SideBarButton>
        <Plus />
        플래너 작성
      </SideBarButton>

      <SideBarSection>
        <SectionTitle>
          USER
          <Ellipsis />
        </SectionTitle>
        <SideBarUser />
      </SideBarSection>

      <SideBarSection>
        <SectionTitle>
          PLANNER <Ellipsis />
        </SectionTitle>
        <SideBarPlanner />
      </SideBarSection>

      <SideBarSection>
        <SectionTitle>
          TODO <Ellipsis />
        </SectionTitle>
      </SideBarSection>
    </SideBarAside>
  );
}

const SideBarAside = styled.aside`
  width: 340px;
  height: 100vh;
  padding: 36px 30px;
  border-right: 1px solid #e3e8ef;
  box-sizing: border-box;
  float: left;
`;

const SideBarSection = styled.section`
  & + & {
    margin-top: 48px;
  }
`;

const SideBarButton = styled.button`
  width: 100%;
  padding: 18px 24px;
  background: #e8f0fc;
  border: none;
  outline: none;
  border-radius: 6px;
  margin-top: 20px;
  margin-bottom: 48px;
  display: flex;
  justify-content: flex-start;
  align-items: flex-start;
  gap: 12px;
  cursor: pointer;

  svg {
    width: 0.95rem;
    height: 0.95rem;
    fill: #3281fc;
  }

  font-size: 0.85rem;
  font-weight: 500;
  color: #3281fc;
`;

const SectionTitle = styled.h3`
  position: relative;
  padding-bottom: 8px;
  border-bottom: 2px solid #ebebeb;
  font-size: 1.05rem;
  font-weight: 650;
  color: #3281fc;

  svg {
    position: absolute;
    bottom: 12px;
    right: 8px;
    width: 1rem;
    height: 1rem;
    fill: #888;
  }
`;

const SideBarUserBox = styled.div`
  padding: 24px 0;
  box-sizing: border-box;

  .profile {
    width: 64px;
    height: 64px;
    border-radius: 50%;
    margin-right: 24px;
    background: #d9d9d9;
    float: left;
  }

  .name {
    margin-top: 14px;
    font-size: 1rem;
    font-weight: 650;
    color: #333;
  }

  .desc {
    margin-top: 8px;
    font-size: 0.85rem;
    font-weight: 600;
    color: #888;
  }
`;

const SideBarPlannerBox = styled.ul`
  padding: 24px 0;
  box-sizing: border-box;

  .planner-item {
    position: relative;
    margin-bottom: 36px;
    font-size: 0.95rem;
    font-weight: 550;
    font-family: Pretendard;
    color: #333;

    &:last-child {
      margin-bottom: 0;
    }

    svg {
      position: absolute;
      bottom: 0px;
      right: 8px;
      width: 1rem;
      height: 1rem;
      fill: #888;
    }
  }
`;
