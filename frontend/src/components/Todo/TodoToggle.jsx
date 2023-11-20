import styled from "styled-components";
import { ReactComponent as ArrowRight } from "../../assets/arrow-right-solid.svg";
import { ReactComponent as ArrowLeft } from "../../assets/arrow-left-solid.svg";
import useCarosel from "./useCarosel";

export default function TodoToggle() {
  const [onToggleRight, onToggleLeft] = useCarosel();

  return (
    <StTodoToggle>
      <ArrowLeftBox onClick={onToggleLeft} />
      <ArrowRightBox onClick={onToggleRight} />
    </StTodoToggle>
  );
}

const StTodoToggle = styled.div`
  width: 100%;
  padding: 24px 60px;
  margin: 12px 0;
  box-sizing: border-box;
  display: flex;
  justify-content: space-between;
`;

const ArrowLeftBox = styled(ArrowLeft)`
  width: 0.95rem;
  height: 0.95rem;
  padding: 10px;
  fill: #bbb;
  background: rgb(247, 248, 250);
`;

const ArrowRightBox = styled(ArrowRight)`
  width: 0.95rem;
  height: 0.95rem;
  padding: 10px;
  fill: #bbb;
  background: rgb(247, 248, 250);
`;
