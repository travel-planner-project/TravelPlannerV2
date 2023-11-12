<script setup lang="ts">
import { ref } from 'vue';
import { library } from '@fortawesome/fontawesome-svg-core';
import { faTriangleExclamation } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { registerUser } from '../../api/AuthAPI';

library.add(faTriangleExclamation);

const email = ref('');
const password = ref('');
const passwordCheck = ref('');
const userNickname = ref('');
const passwordValidationMessage = ref('');
const passwordMatchErrorMessage = ref('');

function isPasswordValid(password) {
  const lengthRegex = /^.{8,15}$/;
  const letterRegex = /[A-Za-z]/;
  const numberRegex = /\d/;
  const specialCharRegex = /[!@#$%^&*]/;

  return (
    lengthRegex.test(password) &&
    letterRegex.test(password) &&
    numberRegex.test(password) &&
    specialCharRegex.test(password)
  );
}

const handleRegister = async () => {
  if (!isPasswordValid(password.value)) {
    passwordValidationMessage.value =
      '비밀번호는 특수문자와 숫자를 포함하여 8 자 이상 이여야 합니다.';
    return;
  }

  if (password.value !== passwordCheck.value) {
    passwordMatchErrorMessage.value = '비밀번호가 일치하지 않습니다.';
    console.log(password.value, passwordCheck.value);
    return;
  }

  const userData = {
    email: email.value,
    password: password.value,
    userNickname: userNickname.value,
  };

  try {
    await registerUser(userData);
  } catch (error) {
    console.log(error);
  }
};
</script>

<template>
  <div class="register_container">
    <div class="title">Sign Up</div>
    <form>
      <div class="input_container">
        <div class="label">Email</div>
        <input v-model="email" type="email" class="input" id="email" />
      </div>
      <div class="input_container">
        <div class="label">Password</div>
        <div v-if="passwordValidationMessage" class="input_warning">
          <font-awesome-icon
            icon="fa-solid fa-triangle-exclamation"
            style="margin-right: 5px"
          />
          <span>{{ passwordValidationMessage }}</span>
        </div>
        <input v-model="password" type="password" class="input" id="password" />
      </div>
      <div class="input_container">
        <div class="label">Password Check</div>
        <div
          v-if="passwordMatchErrorMessage"
          class="input_warning"
          id="passwordCheck"
        >
          <font-awesome-icon
            icon="fa-solid fa-triangle-exclamation"
            style="margin-right: 5px"
          />
          <span>{{ passwordMatchErrorMessage }}</span>
        </div>
        <input v-model="passwordCheck" type="password" class="input" />
      </div>
      <div class="input_container">
        <div class="label">Nickname</div>
        <input v-model="userNickname" type="text" class="input" />
      </div>
      <div>
        <button
          @click.prevent="handleRegister"
          type="submit"
          class="blue-button"
        >
          회원 가입
        </button>
        <button class="gray-button">
          <RouterLink to="/auth/login" style="text-decoration: none"
            >로그인</RouterLink
          >
        </button>
      </div>
    </form>
  </div>
</template>

<style scoped lang="scss">
@import '../../assets/styles/common/Typho';
@import '../../assets/styles/common/container';
@import '../../assets/styles/common/input';
@import '../../assets/styles/common/button';
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
</style>
