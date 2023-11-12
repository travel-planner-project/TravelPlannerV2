<script setup lang="ts">
import { useRoute, useRouter } from 'vue-router';
import { useQuery } from 'vue-query';
import { useAuthStore } from '../../store/AuthStore';
import { ref, onMounted, computed } from 'vue';
import { loginUserInfo } from '../../api/UserAPI';

const route = useRoute();
const router = useRouter();
const authStore = useAuthStore();
const isLoggedIn = computed(() => authStore.isLoggedIn);

const userId = ref('');
const email = ref('');
const nickname = ref('');
const provider = ref('');
const profileImgUrl = ref('');
const token = ref('');

useQuery(['loginUserInfo'], loginUserInfo, {
  enabled: computed(() => isLoggedIn.value),
});

onMounted(() => {
  userId.value = route.query.userId as string;
  email.value = route.query.email as string;
  nickname.value = route.query.nickname as string;
  provider.value = route.query.provider as string;
  profileImgUrl.value = route.query.profileImgUrl as string;
  token.value = route.query.token as string;

  if (token.value) {
    localStorage.setItem('accessToken', 'Bearer ' + token.value);
    authStore.setLoginStatus(true);
  } else {
    console.error('로그인 정보가 누락 되었습니다.');
    authStore.setLoginStatus(false);
    router.push('/login');
  }
});
</script>
