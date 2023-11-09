import axios, { AxiosRequestConfig } from 'axios';

const requestInterceptor = axios.create({
  baseURL: 'https://dev.travel-planner.xyz/api',
});

requestInterceptor.interceptors.request.use(
  (config: AxiosRequestConfig) => {
    const accessToken = localStorage.getItem('Authorization');
    if (accessToken && config.headers) {
      config.headers['Authorization'] = `Bearer ${accessToken}`;
    }

    return config;
  },
  (error: any) => {
    return Promise.reject(error);
  },
);
