import styled from "styled-components";
import FeedList from "./FeedList";

export default function FeedSection() {
  return (
    <StFeedSection>
      <SectionTitle>
        김민승님의 플래너
        <SectionSubTitle>2023.03 ~ 2023.06</SectionSubTitle>
      </SectionTitle>

      <FeedList />
    </StFeedSection>
  );
}

const StFeedSection = styled.section`
  width: calc(100% - 340px);
  height: 100vh;
  padding: 36px 0;
  box-sizing: border-box;
  float: right;
  background: #fff;
`;

const SectionTitle = styled.h1`
  padding: 0 24px;
  padding-bottom: 24px;
  border-bottom: 1px solid #e3e8ef;
  font-size: 1.05rem;
  font-weight: 400;
  color: #888;
`;

const SectionSubTitle = styled.h3`
  margin-top: 24px;
  font-size: 1.4rem;
  font-weight: bold;
  color: #666;
`;
