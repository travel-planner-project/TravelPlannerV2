<script setup lang="ts">
import { computed, ref } from 'vue';
import { useQuery } from 'vue-query';
import { useAuthStore } from '../../store/AuthStore.ts';
import { loginUserInfo } from '../../api/UserAPI.ts';

const email = ref('');
const password = ref('');
const authStore = useAuthStore();
const isLoggedIn = computed(() => authStore.isLoggedIn);

useQuery(['loginUserInfo'], loginUserInfo, {
  enabled: computed(() => isLoggedIn.value),
});

const handleLogin = async () => {
  const userData = {
    email: email.value,
    password: password.value,
  };

  try {
    const response = await localLogin(userData);
    if (response.status == 200) {
      authStore.setLoginStatus(true);
    }
  } catch (error) {
    console.log(error);
  }
};
</script>

<template>
  <div class="register_container">
    <div class="title">Login</div>
    <form>
      <div class="input_container">
        <div class="label">Email</div>
        <input v-model="email" type="email" class="input" id="email" />
      </div>
      <div class="input_container">
        <div class="label">Password</div>
        <input v-model="password" type="password" class="input" id="password" />
      </div>
      <div>
        <button @click.prevent="handleLogin" type="submit" class="blue-button">
          로그인
        </button>
        <button class="gray-button">
          <RouterLink to="/auth/register" style="text-decoration: none"
            >회원가입</RouterLink
          >
        </button>
      </div>
    </form>
    <div class="icon_container">
      <a href="https://dev.travel-planner.xyz/oauth/authorize/google"
        ><img src="../../assets/images/google_login_button.svg" class="icon"
      /></a>
      <a href="https://dev.travel-planner.xyz/oauth/authorize/kakao"
        ><img src="../../assets/images/kakao_login_button.svg" class="icon"
      /></a>
      <a href="https://dev.travel-planner.xyz/oauth/authorize/naver"
        ><img src="../../assets/images/naver_login_button.svg" class="icon"
      /></a>
    </div>
  </div>
</template>

<style scoped lang="scss">
@import '../../assets/styles/common/Typho';
@import '../../assets/styles/common/container';
@import '../../assets/styles/common/input';
@import '../../assets/styles/common/button';
@import '../../assets/styles/common/image';
@import '../../assets/styles/colors/_light';
@import 'bootstrap';

.register_container {
  @include container(column, flex-start, flex-start, auto, 100%);
  padding: 50px 70px;
}

.input_container {
  @include container(column, flex-start, flex-start, auto, auto);
}

.input {
  @include input(350px, 35px, 20px, 0 10px, 8px);
  margin: 0 0 20px 0;
}

.label {
  @include input_label(24px);
  font-family: 'pre-regular', sans-serif;
}

.blue-button {
  @include blue-button(167px, 45px, 5px);
  margin: 20px 15px 0 0;
  font-size: 18px;
  border-radius: 10px;
  font-family: 'pre-semiBold', sans-serif;
  color: $white;
}

.gray-button {
  @include gray-button(167px, 45px, 5px);
  margin: 20px 15px 0 0;
  font-size: 18px;
  border-radius: 10px;
  font-family: 'pre-semiBold', sans-serif;
  color: $black;
}

.input_warning {
  @include input_warning();
  margin: 8px 0;
}

.icon_container {
  @include container(row, center, center, 100%, auto);
  margin-top: 50px;
}
.icon {
  @include image(60px, 0 20px 0 0);
}
</style>
