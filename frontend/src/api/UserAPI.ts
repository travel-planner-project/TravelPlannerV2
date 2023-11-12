import axiosInstance from './interceptor/RequestInterceptor.ts';
export const loginUserInfo = async () => {
  const response = await axiosInstance.get('/users/me');
  return response.data;
};
