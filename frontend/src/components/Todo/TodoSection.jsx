import styled from "styled-components";
import TodoTemplate from "./TodoTemplate";
import TodoToggle from "./TodoToggle";

export default function TodoSection() {
  return (
    <StTodoSection>
      <SectionTitle>
        김민승님의 투두리스트
        <SectionSubTitle>2023.03 ~ 2023.06</SectionSubTitle>
      </SectionTitle>

      <TodoToggle />

      <TodoTemplate />
    </StTodoSection>
  );
}

const StTodoSection = styled.section`
  width: calc(100% - 340px);
  height: 100vh;
  padding: 36px 0;
  box-sizing: border-box;
  float: right;
  background: #fff;
  display: flex;
  flex-direction: column;
  align-items: center;
`;

const SectionTitle = styled.h1`
  width: 100%;
  padding: 0 24px;
  padding-bottom: 24px;
  box-sizing: border-box;
  border-bottom: 1px solid #e3e8ef;
  font-size: 1.05rem;
  font-weight: 400;
  color: #888;
`;

const SectionSubTitle = styled.h3`
  width: 100%;
  margin-top: 24px;
  font-size: 1.4rem;
  font-weight: bold;
  color: #666;
`;
