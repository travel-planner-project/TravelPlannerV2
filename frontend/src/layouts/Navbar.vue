<script setup lang="ts">
import { library } from '@fortawesome/fontawesome-svg-core';
import { faCalendar } from '@fortawesome/free-regular-svg-icons/faCalendar';
import {
  faImages,
  faList,
  faEarthAmericas,
} from '@fortawesome/free-solid-svg-icons';
import { faEnvelope } from '@fortawesome/free-regular-svg-icons/faEnvelope';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';

library.add(faCalendar, faImages, faEnvelope, faList, faEarthAmericas);
</script>

<template>
  <div class="nav-container">
    <div class="logo-container">
      <img src="../assets/images/logo_big.svg" class="logo-image" />
      <div class="line"></div>
    </div>

    <!-- 유저 프로필 이미지 -->
    <div class="img-container">
      <router-link to="/profile">
        <div class="img-contents">
          <img src="../assets/images/user.svg" class="profile-image" />
        </div>
      </router-link>
    </div>

    <!-- 메뉴 리스트 -->
    <div class="menu-container">
      <div class="menu-contents">
        <router-link to="/planner" class="menu-lists" active-class="active">
          <font-awesome-icon icon="fa-regular fa-calendar" class="icon" />
          <span>PLANNER</span>
        </router-link>

        <router-link to="/post" class="menu-lists" active-class="active">
          <font-awesome-icon icon="fa-solid fa-images" class="icon" />
          <span>POST</span>
        </router-link>

        <router-link to="/message" class="menu-lists" active-class="active">
          <font-awesome-icon icon="fa-regular fa-envelope" class="icon" />
          <span>MESSAGE</span>
        </router-link>

        <router-link to="/feed" class="menu-lists" active-class="active">
          <font-awesome-icon icon="fa-solid fa-list" class="icon" />
          <span>FEED</span>
        </router-link>

        <router-link to="/community" class="menu-lists" active-class="active">
          <font-awesome-icon icon="fa-solid fa-earth-americas" class="icon" />
          <span>COMMUNITY</span>
        </router-link>

        <div class="switch-container">
          <label class="switch">
            <input type="checkbox" />
            <span class="slider"></span>
          </label>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped lang="scss">
@import '../assets/styles/common/container.scss';
@import '../assets/styles/common/image.scss';
@import '../assets/styles/colors/_light.scss';

.nav-container {
  @include container(column, flex-start, flex-start, 100%, 100%);
  background-color: $white;
  border-right: 1px solid $black;
  backdrop-filter: blur(20px);
}

.logo-container {
  @include container(column, flex-start, flex-start, 100%, auto);
}

.logo-image {
  @include image(130px, 20px 0 10px 10px);
}

.img-container,
.menu-container,
.menu-contents {
  @include container(column, center, center, 100%, auto);
}

.menu-lists {
  @include container(row, flex-start, center, 70%, auto);
  color: $black;
  font-family: 'pre-semiBold', sans-serif;
  font-size: 20px;
  height: 48px;
  padding: 0 20px;
  margin-bottom: 5px;
  border-radius: 8px;
}

.menu-lists:hover {
  background-color: $blue100;
  color: white;
  text-decoration: none;
}

.profile-image {
  @include image(123px, 85px 0 50px 0);
}

.line {
  width: 100%;
  height: 1px;
  border-top: 1px $black solid;
}

.icon {
  margin-right: 10px;
}

.switch-container {
  @include container(column, flex-start, flex-start, 100%, auto);
  padding: 88px 0 0 60px;
}

.switch {
  position: relative;
  display: inline-block;
  width: 50px;
  height: 24px;
}

.switch input {
  opacity: 0;
  width: 0;
  height: 0;
}

.slider {
  position: absolute;
  cursor: pointer;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: $gray500;
  transition: 0.4s;
  border-radius: 34px;
}

.slider:before {
  position: absolute;
  content: '';
  height: 18px;
  width: 18px;
  left: 4px;
  bottom: 3px;
  background-color: white;
  transition: 0.4s;
  border-radius: 50%;
}

input:checked + .slider {
  background-color: $green100;
}

input:checked + .slider:before {
  transform: translateX(25px);
}
</style>
