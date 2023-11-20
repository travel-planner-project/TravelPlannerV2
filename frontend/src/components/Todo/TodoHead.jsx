import styled from "styled-components";

export default function TodoHead({ label }) {
  return <StTodoHead>{label}</StTodoHead>;
}

const StTodoHead = styled.h3`
  font-size: 1.25rem;
  font-weight: bold;
  color: #666;
  text-align: center;
`;
