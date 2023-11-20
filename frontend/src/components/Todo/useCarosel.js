import { useRecoilState } from "recoil";
import { caroselAtom } from "../../provider/recoil/atom";

export default function useCarosel() {
  const [page, setPage] = useRecoilState(caroselAtom);

  const onToggleRight = () => {
    setPage((prev) => prev - 1);
  };

  const onToggleLeft = () => {
    setPage((next) => next + 1);
  };

  return [onToggleRight, onToggleLeft];
}
