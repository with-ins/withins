<template>
  <div id="banner"></div>
  <RecruitSearchComponent @update-data="handleSearch" />
  <PaginationComponent
      ref="paginationRef"
      :fetch-function="fetchRecruits"
      :url-path="'/recruit'"
      :initial-params="initialSearchParams"
  >
    <!-- 테이블 헤더 -->
    <template #table-top-slot>
      <li>제목</li>
      <li>기관</li>
      <li>모집일</li>
      <li>등록일</li>
    </template>

    <!-- 테이블 내용 -->
    <template #table-result-slot="{ items }">
      <a v-if="!items || items.length <= 0" class="none">검색 결과가 없습니다.</a>
      <RouterLink
          v-else
          v-for="recruit in items"
          :key="recruit.recruitId"
          :to="{
            path: `/recruit/${recruit.recruitId}`,
            state: {
              recruitId: recruit.recruitId,
              returnPath: '/recruit',
              returnQuery: $route.query
            }
          }"
          class="result"
      >
        <span class="tit">{{ recruit.title }}</span>
        <div class="org">
          <span class="org-name">{{ recruit.welfareCenter.name }}</span>
          <div class="org-region">
            <svg width="14" height="14" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M8.00016 7.66665C7.55814 7.66665 7.13421 7.49105 6.82165 7.17849C6.50909 6.86593 6.3335 6.44201 6.3335 5.99998C6.3335 5.55795 6.50909 5.13403 6.82165 4.82147C7.13421 4.50891 7.55814 4.33331 8.00016 4.33331C8.44219 4.33331 8.86611 4.50891 9.17867 4.82147C9.49123 5.13403 9.66683 5.55795 9.66683 5.99998C9.66683 6.21885 9.62372 6.43558 9.53996 6.63779C9.4562 6.83999 9.33344 7.02373 9.17867 7.17849C9.02391 7.33325 8.84018 7.45602 8.63797 7.53978C8.43576 7.62354 8.21903 7.66665 8.00016 7.66665ZM8.00016 1.33331C6.76249 1.33331 5.5755 1.82498 4.70033 2.70015C3.82516 3.57532 3.3335 4.7623 3.3335 5.99998C3.3335 9.49998 8.00016 14.6666 8.00016 14.6666C8.00016 14.6666 12.6668 9.49998 12.6668 5.99998C12.6668 4.7623 12.1752 3.57532 11.3 2.70015C10.4248 1.82498 9.23784 1.33331 8.00016 1.33331Z" fill="#777984"/>
            </svg>
            <span class="org-region-name">{{recruit.welfareCenter.region}}</span>
          </div>
        </div>
        <span class="deadline">{{ recruit.getDeadline() }}</span>
        <span class="createAt">{{ recruit.getCreateAt() }}</span>
      </RouterLink>
    </template>
  </PaginationComponent>
</template>

<script setup lang="ts">
import { ref } from "vue";
import RecruitSearchComponent from "@/components/RecruitSearchComponent.vue";
import PaginationComponent from "@/components/PaginationComponent.vue";
import { ApiServer } from "@/api/ApiServer";
import { Pageable } from "@/domain/Pageable";
import { Recruit } from "@/domain/Recruit";

const paginationRef = ref<InstanceType<typeof PaginationComponent> | null>(null);

// 초기 검색 파라미터
const initialSearchParams = {
  word: '',
  region: 'ALL'
};

// 검색 함수 정의 (페이지네이션 컴포넌트에 전달)
const fetchRecruits = async (page: number, params: any): Promise<Pageable<Recruit>> => {
  // API 호출 준비
  const apiParams = {
    page: page,  // 페이지 인덱스 (0부터 시작)
    condition: {
      region: params.region || 'ALL',
      word: params.word || '',
    }
  };

  // API 호출
  return ApiServer.mockRecruit('');
};

// 검색 이벤트 핸들러
const handleSearch = (searchData: any) => {
  if (paginationRef.value) {
    // 검색 파라미터 형식 변환
    const params = {
      word: searchData.word || '',
      region: searchData.region || 'ALL'
    };

    // 페이지네이션 컴포넌트에 검색 조건 업데이트 요청
    paginationRef.value.updateSearchParams(params);
  }
};
</script>

<style scoped>
#banner {
  aspect-ratio: 24 / 7;
  margin: 30px 0 80px 0;
  border-radius: 20px;
  background-color: #C4C4C4;
}

:deep(#searchTitle),
:deep(.searchResult > a:not(.none)),
:deep(.searchResult > button:not(.none)) {
  grid-template-columns: 40% 30% 20% 10%;
}



.tit {
  font-size: 16px;
  font-weight: 500;
  color: var(--f1);
  display: flex;
  width: 100%;
  align-items: center;
  justify-content: flex-start; /* 왼쪽 정렬 */
  padding-left: 15px; /* 원하는 경우 왼쪽 패딩 추가 */
}
.org {
  text-align: center;
}
.org-name {
  font-size: 15px;
  color: var(--f1);
  font-weight: 400;
}
.org-region {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  margin-top: 6px;
}
.org-region-name {
  font-size: 14px;
  color: var(--f3);
  font-weight: 400;
}
.deadline {
  font-size: 14px;
  color: var(--f2);
}
.createAt {
  font-size: 14px;
  color: var(--f3);
  font-weight: 400;
}

/* Pagination */
.searchResult > a, .searchResult > button {
  display: grid;
  align-items: center;
  justify-items: center;
  height: 80px;
  text-decoration: none;
  width: 100%;
}
.searchResult > a:first-child {
  border-top: 1px #DCDCDC solid;
}
.searchResult > a {
  border-bottom: 1px #DCDCDC solid;
}
.searchResult > a:last-child {
  border-bottom: 1px var(--f1) solid;
}
.searchResult > a:hover:not(#searchTitle > a),
.searchResult > button:hover:not(#searchTitle > a){
  background-color: var(--main-color-1-hover);
  border-radius: 5px;
}
.searchResult > a > li,
.searchResult > button > li {
  display: flex;
  align-items: center;
  justify-content: center;
}

#result.box {
  padding: 24px;
}
</style>