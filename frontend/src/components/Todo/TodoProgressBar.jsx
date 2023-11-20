import styled from "styled-components";

export default function TodoProgressBar() {
  return (
    <ProgressOuter>
      <ProgressInner />
    </ProgressOuter>
  );
}

const ProgressOuter = styled.div`
  width: 100%;
  height: 18px;
  margin: 24px 0;
  border-radius: 14px;
  background: rgb(247, 248, 250);
  overflow: hidden;
`;

const ProgressInner = styled.div`
  width: 50%;
  height: 100%;
  background: rgb(50, 129, 252, 0.5);
`;
