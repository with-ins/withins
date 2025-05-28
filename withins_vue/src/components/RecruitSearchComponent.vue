
<template>
  <div id="recruit-search">
    <div id="wrapper">
      <div id="filters">
        <div class="filter">
          <select id="region" v-model="selectedRegion" @change="searchHandler">
            <option v-for="option in regionOptions" :key="option.value" :value="option.value">
              {{ option.text }}
            </option>
          </select>
        </div>
      </div>
      <div id="search">
        <div id="search-wrapper">
          <svg width="26" height="26" viewBox="0 0 26 26" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path fill-rule="evenodd" clip-rule="evenodd" d="M11.376 3.58301C15.6787 3.58345 19.1668 7.0722 19.167 11.375C19.1669 13.2564 18.4989 14.9818 17.3887 16.3281L22.1973 21.1367L22.248 21.1934C22.4885 21.4879 22.4719 21.9226 22.1973 22.1973C21.9226 22.4719 21.4879 22.4885 21.1934 22.248L21.1367 22.1973L16.3281 17.3887C14.9819 18.4987 13.2571 19.1658 11.376 19.166C7.07287 19.166 3.58318 15.6781 3.58301 11.375C3.58318 7.07193 7.07286 3.58301 11.376 3.58301ZM11.376 5.08301C7.90129 5.08301 5.08416 7.90036 5.08398 11.375C5.08416 14.8496 7.90129 17.666 11.376 17.666C14.8503 17.6656 17.6668 14.8494 17.667 11.375C17.6668 7.90063 14.8503 5.08345 11.376 5.08301Z" fill="#0071E3"/>
          </svg>
          <input v-model="searchKeyword" @keyup.enter="searchHandler" type="text" name="word" placeholder="검색어를 입력해주세요.">
        </div>
      </div>
    </div>

  </div>
</template>

<script setup lang="ts">
import {onMounted, ref} from 'vue';
import {useRoute} from "vue-router";

const selectedRegion = ref('ALL');
const regionOptions = [
  { value: 'ALL', text: '전국' },
  { value: 'SEOUL', text: '서울' },
  { value: 'BUCHEON', text: '부천' },
  { value: 'INCHEON', text: '인천' }
];

const emit = defineEmits(['update-data']);

const searchKeyword = ref('');

const searchHandler = () => {
  let data = {
    word : searchKeyword.value,
    region : selectedRegion.value,
  }
  emit('update-data', data);
}

const route = useRoute();

onMounted(() => {
  if (route.query.word) {
    searchKeyword.value = route.query.word as string;
  }
  if (route.query.region) {
    selectedRegion.value = route.query.region as string;
  }
})
</script>

<style scoped>
#recruit-search {
  margin-bottom: 20px;
}
#recruit-search #wrapper {
  display: flex;
  gap: 15px;
  justify-content: center;
}
.filter {
  flex: 1;
}

.filter select {
  height: 45px;
  padding: 12px 48px 12px 24px;
  border-radius: 100px;
  border: 1px solid var(--main-color-1);
  box-shadow: 10px 10px 30px rgba(0, 0, 0, 0.06);
  /* 브라우저 기본 스타일 제거 */
  appearance: none;
  -webkit-appearance: none;
  -moz-appearance: none;
  /* 커스텀 화살표 추가 */
  background-image: url("data:image/svg+xml;utf8,<svg fill='black' height='24' viewBox='0 0 24 24' width='24' xmlns='http://www.w3.org/2000/svg'><path d='M7 10l5 5 5-5z'/></svg>");
  background-position: right 10px center, 0 0, 0 0;
  background-repeat: no-repeat;
}

#search {
  height: 45px;
  width: 500px;
  border-radius: 100px;
  border: 1px solid transparent;
  background-image: linear-gradient(#fff, #fff), var(--main-color-gradient);
  background-origin: border-box;
  background-clip: content-box, border-box;
  box-shadow: 10px 10px 30px rgba(0, 0, 0, 0.06);
}
#search-wrapper {
  height: 100%;
  display: flex;
  gap: 16px;
  align-items: center;
  padding: 0 24px;
}
#search input {
  width: 100%;
}
</style>