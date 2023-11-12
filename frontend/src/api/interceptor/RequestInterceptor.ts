import axios from 'axios';

const axiosInstance = axios.create({
  baseURL: 'https://dev.travel-planner.xyz/api',
});

axiosInstance.interceptors.request.use(
  config => {
    const token = localStorage.getItem('accessToken');
    if (token) {
      config.headers.authorization = token;
    }
    return config;
  },
  error => {
    return Promise.reject(error);
  },
);

export default axiosInstance;
