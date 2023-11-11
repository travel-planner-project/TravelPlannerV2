import { createWebHistory, createRouter } from 'vue-router';

const routes = [
  {
    path: '/auth/register',
    component: () => import('../components/user/UserRegister.vue'),
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
