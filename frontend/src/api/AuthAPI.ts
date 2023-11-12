import axios from 'axios';

const API_BASE_URL = 'https://dev.travel-planner.xyz/api/auth';

const apiClient = axios.create({
  baseURL: API_BASE_URL,
});

export const registerUser = async (userData: {
  userNickname: string;
  email: string;
  password: string;
}) => {
  const response = await apiClient.post('/register', userData);
  return response.data;
};

export const localLogin = async (loginData: {
  email: string;
  password: string;
}) => {
  const response = await apiClient.post('/login', loginData);
  const accessToken = response.headers.authorization;

  if (accessToken) {
    localStorage.setItem('accessToken', accessToken);
  }

  return response;
};

export const logout = async () => {
  await apiClient.post('/logout');
};
