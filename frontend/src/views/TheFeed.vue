<script setup lang="ts">
import { library } from '@fortawesome/fontawesome-svg-core';
import { faCalendarCheck } from '@fortawesome/free-regular-svg-icons';
import { faUserGroup } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { onMounted, ref } from 'vue';
import { feed, feedSearch } from '../api/FeedAPI';
import InfiniteLoading from 'v3-infinite-loading';

library.add(faCalendarCheck, faUserGroup);

const feedData = ref([]);
const searchQuery = ref('');
const pageSize = 6;
let page = ref(0);

const loadData = async () => {
  let response;
  if (searchQuery.value) {
    // 검색 쿼리가 있을 때, 현재 페이지 번호를 사용하여 검색
    response = await feedSearch(searchQuery.value, page.value, pageSize);
  } else {
    // 검색 쿼리가 없을 때, 현재 페이지 번호를 사용하여 일반 피드 로드
    response = await feed(page.value, pageSize);
  }

  if (response.data.length < pageSize) {
    return { complete: true };
  } else {
    if (
      !feedData.value.some(
        item => item.plannerId === response.data.content[0].plannerId,
      )
    ) {
      feedData.value.push(...response.data.content);
      page.value++;
    }
    return { loaded: true };
  }
};

const resetAndSearch = () => {
  // 검색 시 페이지 번호와 feedData 초기화
  feedData.value = [];
  page.value = 0;
  loadData();
};

const infiniteHandler = async $state => {
  const { loaded } = await loadData();
  if (loaded) $state.loaded();
};

// const infiniteHandler = async $state => {
//   try {
//     const { loaded, complete } = await loadData();
//     if (complete) $state.complete();
//     else if (loaded) $state.loaded();
//   } catch (error) {
//     $state.error();
//   }
// };

const handleSearch = () => {
  resetAndSearch();
};

onMounted(() => {
  resetAndSearch();
});
</script>

<template>
  <div class="feed_container">
    <div class="title">FEED</div>
    <div class="search_container">
      <input type="search" class="input" v-model="searchQuery" />
      <button @click="handleSearch" class="blue-button">검색</button>
    </div>
    <div class="feed_planner_container">
      <div class="feed_content" v-for="(feed, index) in feedData" :key="index">
        <div class="feed_content_adjust">
          <div>
            <img v-if="feed.hostUrl" :src="feed.hostUrl" class="feed_image" />
            <img v-else src="../assets/images/user.svg" class="feed_image" />
          </div>
          <div class="feed_title">{{ feed.planTitle }}</div>
        </div>
        <div class="feed_content_adjust">
          <div class="feed_content_adjust" style="margin-right: 15px">
            <font-awesome-icon
              icon="fa-regular fa-calendar-check"
              class="feed_icon feed_planner_content_colored"
              style="font-size: 22px; margin-right: 6px"
            />
            <span class="feed_planner_content_colored" style="color: #1e1e1c">
              23. 11. 24 ~ 23. 11. 27
            </span>
          </div>
          <div class="feed_content_adjust">
            <font-awesome-icon
              icon="fa-solid fa-user-group"
              class="feed_icon"
            />
            <span class="feed_planner_content_colored">4 </span>
            <span class="feed_planner_content">명이 함께 해요</span>
          </div>
        </div>
      </div>
      <InfiniteLoading @infinite="infiniteHandler"></InfiniteLoading>
    </div>
  </div>
</template>

<style lang="scss">
@import '../assets/styles/common/container.scss';
@import '../assets/styles/common/Typho.scss';
@import '../assets/styles/common/input.scss';
@import '../assets/styles/common/button.scss';
@import '../assets/styles/common/image.scss';
@import '../assets/styles/colors/_light.scss';

.feed_container {
  @include container(column, flex-start, flex-start, 100%, 100%);
}

.title {
  margin: 60px 0 40px 70px;
}

.search_container {
  @include container(row, flex-start, center, 100%, auto);
}

.input {
  @include input(500px, 35px, 20px, 0 10px, 10px);
  margin-right: 20px;
  margin-left: 70px;
}

.blue-button {
  @include blue-button(100px, 35px, 5px);
  font-size: 18px;
  border-radius: 10px;
  font-family: 'pre-semiBold', sans-serif;
  color: $white;
}

.feed_planner_container {
  @include container(column, flex-start, flex-start, 70%, 480px);
  margin-top: 30px;
  margin-left: 70px;
  overflow-y: scroll;
}

.feed_image {
  @include image(45px, 0 15px 0 0);
  border-radius: 50%;
}

.feed_title {
  font-family: 'pre-semiBold', sans-serif;
  font-size: 28px;
}

.feed_icon {
  color: $gray600;
  font-size: 18px;
  margin-right: 5px;
}

.feed_content {
  @include container(row, space-between, center, 90%, auto);
  padding: 10px 20px;
  background-color: $white;
  border-radius: 15px;
  border: 1px $black solid;
  margin-bottom: 10px;
}

.feed_content_adjust {
  @include container(row, flex-start, center, auto, auto);
}

.feed_planner_content {
  @include container(row, flex-start, center, 100%, auto);
  font-size: 16px;
}

.feed_planner_content_colored {
  font-family: 'pre-semiBold', sans-serif;
  color: $pink100;
}
</style>
