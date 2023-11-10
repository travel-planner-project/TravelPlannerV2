<script setup lang="ts"></script>

<template>
  <div class="content-container"></div>
</template>

<style scoped lang="scss">
@import '../assets/styles/common/container.scss';
@import '../assets/styles/colors/_light.scss';

.content-container {
  @include container(column, center, center, 100%, 100%);
  background-color: transparent;
}
</style>
