import { createWebHistory, createRouter } from 'vue-router';

const routes = [
  {
    path: '/register',
    component: () => import('../views/user/UserRegister.vue'),
  },
  {
    path: '/login',
    component: () => import('../views/user/UserLogin.vue'),
  },
  {
    path: '/oauth/callback',
    component: () => import('../views/user/OAuthLogin.vue'),
  },
  {
    path: '/feed',
    component: () => import('../views/TheFeed.vue'),
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
