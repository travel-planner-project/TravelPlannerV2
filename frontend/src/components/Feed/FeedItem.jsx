import styled from "styled-components";
import { ReactComponent as Lock } from "../../assets/lock-solid.svg";

export default function FeedItem() {
  return (
    <StFeedItem>
      <ItemTitle>
        <Lock />
        부산 여행 플래너...!
      </ItemTitle>

      <ItemUsers>
        <ItemUser />
        <ItemUser />
        <ItemUser />
      </ItemUsers>

      <ItemCount>
        <span>5</span> 명
      </ItemCount>

      <ItemInfo>2023.03 ~ 2023.06</ItemInfo>
    </StFeedItem>
  );
}

const StFeedItem = styled.li`
  position: relative;
  width: 100%;
  height: 140px;
  padding: 24px;
  box-sizing: border-box;
  border-radius: 0.5rem;
  border: 2px solid #e8e8e8;
  background: #fff;
`;

const ItemTitle = styled.h2`
  margin-bottom: 20px;
  font-size: 1.05rem;
  font-weight: bold;
  color: #333;
  cursor: pointer;

  &:hover {
    color: #3281fc;
  }

  svg {
    width: 0.8rem;
    height: 1rem;
    margin-right: 8px;
    fill: #d9d9d9;
  }
`;

const ItemUsers = styled.div`
  position: absolute;
  bottom: 18px;
  left: 24px;
  display: flex;
  gap: 8px;
`;

const ItemUser = styled.div`
  width: 30px;
  height: 30px;
  border-radius: 50%;
  background: #d9d9d9;
`;

const ItemInfo = styled.h3`
  position: absolute;
  bottom: 24px;
  right: 24px;
  font-size: 0.825rem;
  font-weight: 550;
  color: #888;
`;

const ItemCount = styled.h3`
  position: absolute;
  bottom: 22px;
  right: 160px;
  font-size: 0.825rem;
  font-weight: 550;
  color: #888;

  span {
    font-size: 0.925rem;
    font-weight: 550;
    color: #3281fc;
  }
`;
