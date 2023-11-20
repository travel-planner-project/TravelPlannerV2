import { useState } from "react";

export default function useInput(initialState) {
  const [input, setInput] = useState(initialState);

  const onChange = (e) => setInput(e.target.value);

  const refresh = () => setInput(initialState);

  return [input, onChange, refresh];
}
