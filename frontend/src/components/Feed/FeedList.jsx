import styled from "styled-components";
import FeedItem from "./FeedItem";

export default function FeedList() {
  return (
    <StFeedList>
      <FeedItem />

      <FeedItem />

      <FeedItem />
    </StFeedList>
  );
}

const StFeedList = styled.ul`
  height: 100%;
  padding: 48px 24px;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 36px;
`;
