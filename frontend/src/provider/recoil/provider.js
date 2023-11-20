import { RecoilRoot } from "recoil";

export default function TodoProvider({ children }) {
  return <RecoilRoot>{children}</RecoilRoot>;
}
