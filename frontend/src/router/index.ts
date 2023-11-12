import { createWebHistory, createRouter } from 'vue-router';

const routes = [
  {
    path: '/auth/register',
    component: () => import('../components/user/UserRegister.vue'),
  },
  {
    path: '/auth/login',
    component: () => import('../components/user/UserLogin.vue'),
  },
  {
    path: '/oauth/callback',
    component: () => import('../components/user/OAuthLogin.vue'),
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
