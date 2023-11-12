import { defineStore } from 'pinia';

export const useAuthStore = defineStore('auth', {
  state: () => ({
    isLoggedIn: false,
  }),
  actions: {
    setLoginStatus(status: boolean) {
      this.isLoggedIn = status;
    },
  },
});
