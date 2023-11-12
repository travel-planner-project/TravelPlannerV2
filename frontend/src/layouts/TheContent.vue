<script setup lang="ts"></script>

<template>
  <div class="content-container">
    <router-view />
  </div>
</template>

<style scoped lang="scss">
@import '../assets/styles/common/container.scss';
@import '../assets/styles/colors/_light.scss';

.content-container {
  @include container(column, flex-start, flex-start, 100%, 100%);
  background-color: transparent;
}
</style>
