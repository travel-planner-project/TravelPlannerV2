import { atom } from "recoil";

export const todosAtom = atom({
  key: "todos",
  default: [],
});

export const todoAtom = atom({
  key: "todo",
  default: {},
});

export const caroselAtom = atom({
  key: "carosel",
  default: 0,
});
