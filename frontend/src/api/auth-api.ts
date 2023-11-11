import axios from 'axios';

const API_BASE_URL = 'https://dev.travel-planner.xyz/api';

const apiClient = axios.create({
  baseURL: API_BASE_URL,
});

export const registerUser = async (userData: {
  userNickname: string;
  email: string;
  password: string;
}) => {
  const response = await apiClient.post('/auth/register', userData);
  return response.data;
};

export const login = async (loginData: { email: string; password: string }) => {
  await apiClient.post('/auth/login', loginData);
};

export const logout = async () => {
  await apiClient.post('/auth/logout');
};
