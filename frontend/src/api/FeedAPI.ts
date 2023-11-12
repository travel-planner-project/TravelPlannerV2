import axios from 'axios';

const API_BASE_URL = 'https://dev.travel-planner.xyz/api';

const apiClient = axios.create({
  baseURL: API_BASE_URL,
});

export const feed = async (page: number, size: number) => {
  const response = await apiClient.get('/feed', {
    params: { page, size },
  });
  return response;
};

export const feedSearch = async (query: string, page: number, size: number) => {
  const response = await apiClient.get('/feed', {
    params: { planTitle: query, page, size },
  });
  return response;
};
