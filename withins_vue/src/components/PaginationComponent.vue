<template>
  <div id="pagination">
    <!-- 테이블 컨텐츠 부분 (슬롯으로 제공) -->
    <div id="result" class="box">
      <ul class="searchResult" id="searchTitle">
        <slot name="table-top-slot"></slot>
      </ul>
      <ul class="searchResult" id="searchResult">
        <slot name="table-result-slot" :items="items"></slot>
      </ul>
    </div>

    <!-- 페이지네이션 UI 부분 -->
    <div class="pagination">
      <button @click="goToFirst()" :disabled="currentPage <= 1" type="button" class="button_first">
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512"><path d="M41.4 233.4c-12.5 12.5-12.5 32.8 0 45.3l160 160c12.5 12.5 32.8 12.5 45.3 0s12.5-32.8 0-45.3L109.3 256 246.6 118.6c12.5-12.5 12.5-32.8 0-45.3s-32.8-12.5-45.3 0l-160 160zm352-160l-160 160c-12.5 12.5-12.5 32.8 0 45.3l160 160c12.5 12.5 32.8 12.5 45.3 0s12.5-32.8 0-45.3L301.3 256 438.6 118.6c12.5-12.5 12.5-32.8 0-45.3s-32.8-12.5-45.3 0z"/></svg>
      </button>
      <button @click="goToPrev()" :disabled="currentPage <= 1" type="button" class="button_previous">
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 320 512"><path d="M41.4 233.4c-12.5 12.5-12.5 32.8 0 45.3l160 160c12.5 12.5 32.8 12.5 45.3 0s12.5-32.8 0-45.3L109.3 256 246.6 118.6c12.5-12.5 12.5-32.8 0-45.3s-32.8-12.5-45.3 0l-160 160z"/></svg>
      </button>
      <ol class="page_list">
        <li v-for="i in pageButtons" :key="i" class="page" :class="{selected : i === currentPage}">
          <button @click="goToPage(i)" type="button" class="page_link">{{ i }}</button>
        </li>
      </ol>
      <button @click="goToNext()" :disabled="currentPage >= totalPages" type="button" class="button_next">
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 320 512"><path d="M278.6 233.4c12.5 12.5 12.5 32.8 0 45.3l-160 160c-12.5 12.5-32.8 12.5-45.3 0s-12.5-32.8 0-45.3L210.7 256 73.4 118.6c-12.5-12.5-12.5-32.8 0-45.3s32.8-12.5 45.3 0l160 160z"/></svg>
      </button>
      <button @click="goToLast()" :disabled="currentPage >= totalPages" type="button" class="button_last">
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512"><path d="M470.6 278.6c12.5-12.5 12.5-32.8 0-45.3l-160-160c-12.5-12.5-32.8-12.5-45.3 0s-12.5 32.8 0 45.3L402.7 256 265.4 393.4c-12.5 12.5-12.5 32.8 0 45.3s32.8 12.5 45.3 0l160-160zm-352 160l160-160c12.5-12.5 12.5-32.8 0-45.3l-160-160c-12.5-12.5-32.8-12.5-45.3 0s-12.5 32.8 0 45.3L210.7 256 73.4 393.4c-12.5 12.5-12.5 32.8 0 45.3s32.8 12.5 45.3 0z"/></svg>
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";

// Props 정의
const props = defineProps({
  // 데이터 로드 함수 (필수)
  fetchFunction: {
    type: Function,
    required: true
  },
  // 페이지당 항목 수
  pageSize: {
    type: Number,
    default: 10
  },
  // 페이지 버튼 그룹의 크기
  buttonCount: {
    type: Number,
    default: 10
  },
  // URL 경로 (페이지네이션 상태를 URL에 유지)
  urlPath: {
    type: String,
    default: ''
  },
  // 검색어 파라미터 필드명
  searchField: {
    type: String,
    default: 'word'
  },
  // 초기 검색 파라미터
  initialParams: {
    type: Object,
    default: () => ({})
  }
});

// 로컬 상태
const currentPage = ref(1);
const totalPages = ref(1);
const totalItems = ref(0);
const items = ref<any[]>([]);
const searchParams = ref<Record<string, any>>({});

// 라우터
const route = useRoute();
const router = useRouter();

// 페이지 버튼 목록 계산
const pageButtons = computed(() => {
  const pageCount = Math.floor((currentPage.value - 1) / props.buttonCount);
  const startNum = Math.max(1, pageCount * props.buttonCount + 1);

  const list = [];
  for (let i = startNum; i < startNum + props.buttonCount && i <= totalPages.value; i++) {
    list.push(i);
  }
  return list;
});

// 컴포넌트 마운트 시 URL에서 상태 복원
onMounted(() => {
  // URL 쿼리 파라미터에서 페이지와 검색 조건 복원
  const pageParam = route.query.page ? Number(route.query.page) : 1;
  currentPage.value = pageParam;

  // 초기 검색 파라미터 설정
  searchParams.value = { ...props.initialParams };

  // URL에서 검색 파라미터 복원
  for (const key in route.query) {
    if (key !== 'page') {
      searchParams.value[key] = route.query[key];
    }
  }

  // 초기 데이터 로드
  loadData();
});

// 페이지 이동 함수
const goToFirst = () => navigateToPage(1);
const goToPrev = () => navigateToPage(Math.max(1, currentPage.value - 1));
const goToNext = () => navigateToPage(Math.min(totalPages.value, currentPage.value + 1));
const goToLast = () => navigateToPage(totalPages.value);
const goToPage = (page: number) => navigateToPage(page);

// 검색 파라미터 업데이트 함수
function updateSearchParams(params: Record<string, any>) {
  searchParams.value = { ...params };
  navigateToPage(1); // 검색 조건 변경 시 1페이지로 이동
}

// 페이지 이동 함수 (URL 업데이트 포함)
function navigateToPage(page: number) {
  window.scrollTo({
    top: 0,
    behavior: 'auto'
  });
  currentPage.value = page;
  updateUrl();
  loadData();
}

// URL 업데이트 함수
function updateUrl() {
  if (!props.urlPath) return; // URL 경로가 지정되지 않은 경우 무시

  const query: Record<string, string> = {
    page: currentPage.value.toString()
  };

  // 검색 파라미터 추가
  for (const key in searchParams.value) {
    if (searchParams.value[key] !== undefined && searchParams.value[key] !== '') {
      query[key] = String(searchParams.value[key]);
    }
  }

  // 현재 URL을 업데이트 (히스토리에 추가)
  router.push({
    path: props.urlPath,
    query: query
  });
}

// 데이터 로드 함수
async function loadData() {
  try {
    // API는 0부터 시작하는 페이지 번호를 사용한다고 가정
    const apiPageIndex = currentPage.value - 1;

    // fetchFunction 호출
    const result = await props.fetchFunction(apiPageIndex, searchParams.value);

    // 결과 처리
    if (result) {
      items.value = result.content || [];
      totalItems.value = result.totalElements || 0;
      totalPages.value = result.totalPages || 1;

      // 현재 페이지가 범위를 벗어나면 조정
      if (currentPage.value > totalPages.value && totalPages.value > 0) {
        navigateToPage(totalPages.value);
      }
    }
  } catch (error) {
    console.error('Error loading paginated data:', error);
    items.value = [];
    totalItems.value = 0;
    totalPages.value = 1;
  }
}

// 라우터 변경 감지 (뒤로가기/앞으로가기 처리)
watch(
    () => route.query,
    (newQuery) => {
      if (newQuery.page && Number(newQuery.page) !== currentPage.value) {
        currentPage.value = Number(newQuery.page);

        // 검색 파라미터 업데이트
        for (const key in newQuery) {
          if (key !== 'page') {
            searchParams.value[key] = newQuery[key];
          }
        }

        // 데이터 다시 로드
        loadData();
      }
    }
);

// 외부에서 접근 가능한 메서드 노출
defineExpose({
  updateSearchParams,
  navigateToPage,
  currentPage,
  totalPages,
  totalItems
});
</script>


<style scoped>

/* 페이지네이션 */
.pagination {
  display: flex;
  justify-content: center;
  margin-top: 16px;
  background-color: #ffffff00;
}

.pagination svg {
  fill: var(--f1);
  width: 100%;
  height: 100%;
}

.pagination button:disabled svg {
  fill: rgba(16, 16, 16, 0.3);
  pointer-events: none;
}
.pagination button {
  display: flex;
  width: 24px;
  height: 24px;
  justify-content: center;
  align-items: center;
  padding: 0;
  margin: 0;
  cursor: pointer;
  background-color: rgba(255, 255, 255, 0);
  border: none;
}
.pagination button:disabled {
  cursor: default;
}

.pagination .button_previous {
  margin-left: 10px;
}

.pagination .button_next {
  margin-right: 10px;
}

.pagination .page_list {
  padding: 0 14px;
  margin: 0 15px;
  white-space: nowrap;
  font-size: 0;
}

.pagination .page {
  display: inline-block;
}

.pagination .page + .page {
  margin-left: 6px;
}

.pagination .page.selected .page_link {
  border: 1px var(--main-color-1) solid;
  background: white;
  color: var(--main-color-1);
}


.pagination .page_link {
  position: relative;
  display: block;
  min-width: 30px;
  height: 28px;
  border: 1px solid transparent;
  border-radius: 5px;
  padding: 0 7px;
  font-size: 14px;
  font-weight: normal;
  line-height: 26px;
  color: #303038;
  text-align: center;
}

#searchTitle {
  display: grid;
  align-items: center;
  justify-items: center;
  height: 50px;
  background-color: #F5F6F8;
  border-top: 1px solid var(--f1);
}

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