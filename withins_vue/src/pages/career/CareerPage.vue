
<template>
  <div id="career-profile">
    <div id="hero">
      <h1>맞춤형 강사 찾기</h1>
      <h3>프로그램에 딱 맞는 전문성을 한눈에</h3>
    </div>
    <div id="career">
      <CareerPaginationComponent
        ref="paginationRef"
        :fetch-function="fetchCareers"
        :url-path="'/career'"
        :initial-params="initialSearchParams"
      >

        <!-- 테이블 내용 -->
        <template #table-result-slot="{ items }">
          <a v-if="!items || items.length <= 0" class="none">검색 결과가 없습니다.</a>
          <RouterLink
              v-else
              v-for="career in items"
              :key="career.careerId"
              :to="{
            path: `/career/${career.careerId}`,
            state: {
              careerId: career.careerId,
              returnPath: '/career',
              returnQuery: $route.query
            }
          }"
              class="result"
          >
            <div class="img-wrap">
              <img v-if="career.imageUrl != null" :src="career.imageUrl" alt="프로필">
            </div>
            <div class="profile-wrap">
              <div class="profile">
                <h3 class="summary">{{career.summary}}</h3>
                <pre class="experience">{{career.experience}}</pre>
              </div>
              <div class="tags">
                <div class="tag" v-for="(a, index) in career.tags" :key="a + index">{{a}}</div>
              </div>
            </div>
          </RouterLink>
        </template>
      </CareerPaginationComponent>
      <div id="condition">
        <input type="hidden" v-model="sort">
        <div id="select-sort-wrap">
          <button id="select-sort" @click="showSortModal = !showSortModal">
            <span>{{sortMap[sort]}}</span>
            <svg width="15" height="16" viewBox="0 0 15 16" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path fill-rule="evenodd" clip-rule="evenodd" d="M4.93161 5.93185C4.75587 6.10759 4.75587 6.39251 4.93161 6.56825C5.10735 6.74398 5.39227 6.74398 5.56801 6.56825L7.49981 4.63644L9.43161 6.56825C9.60735 6.74398 9.89227 6.74398 10.068 6.56825C10.2437 6.39251 10.2437 6.10759 10.068 5.93185L7.81801 3.68185C7.73361 3.59746 7.61915 3.55005 7.49981 3.55005C7.38046 3.55005 7.266 3.59746 7.18161 3.68185L4.93161 5.93185ZM10.068 10.0682C10.2437 9.89251 10.2437 9.60759 10.068 9.43185C9.89227 9.25612 9.60735 9.25612 9.43161 9.43185L7.49981 11.3637L5.56801 9.43185C5.39227 9.25612 5.10735 9.25612 4.93161 9.43185C4.75587 9.60759 4.75587 9.89251 4.93161 10.0682L7.18161 12.3183C7.35735 12.494 7.64227 12.494 7.81801 12.3183L10.068 10.0682Z" fill="#868E96"/>
            </svg>
          </button>
          <div class="modal" v-if="showSortModal">
            <button type="button" class="modal-item" :class="{'current' : sort == key}" v-for="(value, key) in sortMap" :key="key" @click="selectSort(key)">{{value}}</button>
          </div>
        </div>
        <div class="br"></div>
        <div id="skill-search-btn">
          <svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path fill-rule="evenodd" clip-rule="evenodd" d="M7.00075 2.20483C9.64861 2.2051 11.7951 4.35201 11.7952 6.99991C11.7952 8.15772 11.3841 9.21945 10.7009 10.048L13.66 13.0071L13.6913 13.042C13.8392 13.2233 13.829 13.4908 13.66 13.6598C13.491 13.8288 13.2235 13.839 13.0422 13.691L13.0074 13.6598L10.0482 10.7006C9.21979 11.3837 8.15837 11.7943 7.00075 11.7944C4.35268 11.7944 2.20519 9.64795 2.20508 6.99991C2.20516 4.35184 4.35267 2.20483 7.00075 2.20483ZM7.00075 3.12791C4.86247 3.12791 3.12884 4.86164 3.12876 6.99991C3.12886 9.13815 4.86248 10.8713 7.00075 10.8713C9.13879 10.871 10.872 9.13798 10.8721 6.99991C10.8721 4.86181 9.1388 3.12818 7.00075 3.12791Z" fill="#45495E"/>
          </svg>
          <input type="search" @input="search = $event.target.value" name="skill" id="skill" placeholder="직무 검색">
        </div>
        <div id="tagList">
          <button type="button" @click="selectTag(tag)" class="tag-btn" :class="{'selected' : tags.includes(tag)}" v-for="tag in sortedTagList" :key="tag">{{tag}}</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">

import {ref, computed, onMounted, watch} from "vue";
import { Pageable } from "@/domain/Pageable";
import { ApiServer } from "@/api/ApiServer";
import { Career } from "@/domain/Career";
import CareerPaginationComponent from "@/components/CareerPaginationComponent.vue";
import {useRoute} from "vue-router";

const paginationRef = ref<InstanceType<typeof CareerPaginationComponent> | null>(null);

const sortMap = {
  'POPULAR': '인기순',
  'LIKE': '추천수 순',
  'VIEW': '조회수 순',
  'LATEST': '최신순'
};

const originalTagList = [
  '노래', '미디어', '학대예방', 'CS', '경찰연계', '자기계발', '건강증진'
];

// 초기 검색 파라미터
const initialSearchParams = {
  sort: 'POPULAR',
  tags: []
};

const search = ref('');
const sort = ref('POPULAR');
const tags = ref<string[]>([]);
const showSortModal = ref(false);

// 검색어를 기반으로 필터링된 태그 목록
const filteredTagList = computed(() => {
  if (!search.value) {
    return originalTagList;
  }
  return originalTagList.filter(tag =>
      tag.toLowerCase().includes(search.value.toLowerCase())
  );
});

// 선택된 태그가 앞에 오도록 정렬된 태그 목록
const sortedTagList = computed(() => {
  return [...filteredTagList.value].sort((a, b) => {
    if (tags.value.includes(a) && !tags.value.includes(b)) return -1;
    if (!tags.value.includes(a) && tags.value.includes(b)) return 1;
    return originalTagList.indexOf(a) - originalTagList.indexOf(b);
  });
});

const selectSort = (key: string) => {
  sort.value = key;
  showSortModal.value = false;
  handleSearch();
};
const selectTag = (tag: string) => {
  console.log('선택될때 현재 태그 목록 :', tags.value)
  const index = tags.value.indexOf(tag);
  if (index === -1) {
    // 태그가 없으면 추가
    tags.value.push(tag);
  } else {
    // 태그가 있으면 제거
    tags.value.splice(index, 1);
  }
  handleSearch();
};





const fetchCareers = async (page: number, params: any): Promise<Pageable<Career>> => {
  // API 호출 준비
  const apiParams = {
    page: page,  // 페이지 인덱스 (0부터 시작)
    condition: {
      sort: sort.value,
    }
  };

  // API 호출
  return ApiServer.mockCareer('');
};
// 검색 이벤트 핸들러
const handleSearch = () => {
  if (paginationRef.value) {
    // 검색 파라미터 형식 변환
    const params = {
      sort: sort.value,
      tags: tags.value
    };

    // 페이지네이션 컴포넌트에 검색 조건 업데이트 요청
    paginationRef.value.updateSearchParams(params);
  }
};

const route = useRoute();

// 초기화 시 URL의 쿼리 파라미터에서 상태 복원
onMounted(() => {
  // URL에서 상태 복원
  if (route.query.sort) {
    sort.value = route.query.sort as string;
  }

  if (route.query.tags) {
    let parsedTags: string[] = [];

    if (Array.isArray(route.query.tags)) {
      parsedTags = route.query.tags.map(tag => tag.trim());
    } else {
      // 쉼표로 구분된 문자열을 배열로 변환하고 각 항목 트림
      parsedTags = (route.query.tags as string).split(',').map(tag => tag.trim());
    }

    tags.value = parsedTags;
  }
});


</script>

<style scoped>
.modal {
  position: absolute;
  z-index: 5;
  width: 100%;
  border-radius: 8px;
  border: 1px #E9ECEF solid;
  padding: 5px;
  display: flex;
  flex-direction: column;
  gap: 2px;
  top: 120%;
  background-color: white;
  box-shadow: 4px 4px 8px rgba(0,0,0,0.06);
}
.modal-item {
  height: 36px;
  border-radius: 8px;
  padding: 10px 12px;
  display: flex;
  justify-content: start;
  color: var(--f2);
}
.modal-item:hover:not(.current) {
  background-color: #f5f5f5;
}
.modal-item.current {
  color: var(--main-color-1);
  background-color: #DFEFFF;
}
/* Hero */
#hero {
  padding: 80px 0 100px 0;
  text-align: center;
}
#hero h1 {
  font-size: 40px;
  font-weight: 700;
  color: var(--f1);
  margin-bottom: 12px;
}
#hero h3 {
  font-size: 21px;
  font-weight: 300;
  color: var(--f3);
}

/* Career */
#career {
  display: flex;
  gap: 30px;
  min-height: 100vh;
}
#pagination {
  flex: 1;
}
#condition {
  width: 240px;
  height: fit-content;
  min-height: 400px;
  border-radius: 16px;
  border: 1px #E9ECEF solid;
  padding: 20px;

  top: 20px;
  position: sticky;
}
#condition > .br {
  height: 1px;
  width: 100%;
  background-color: #D9D9D9;
  margin: 24px 0;
}

#select-sort-wrap {
  position: relative;
}
#select-sort {
  width: 100%;
  height: 36px;
  display: flex;
  justify-content: space-between;
  align-items: center;

  border: 1px #CED4DA solid;
  border-radius: 8px;
  padding: 0 12px;
}

#skill-search-btn {
  width: 100%;
  height: 36px;
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 0 12px;
  border: 1px #CED4DA solid;
  border-radius: 8px;
}
#skill {
  width: 100%;
}

#tagList {
  margin-top: 6px;
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
}
.tag-btn {
  border: 1px #CED4DA solid;
  border-radius: 8px;
  color: var(--f2);
  height: 36px;
  padding: 0 20px;
}
.tag-btn.selected {
  border: 1px var(--main-color-1) solid;
  color: var(--main-color-1);
}


/* Pagination */
.result {
  padding: 10px;
  display: flex;
  gap: 15px;
  min-height: 180px;
  transition: transform 0.2s ease-in-out;
}
.result:hover {
  transform: translateY(-2px);
}

.img-wrap {
  aspect-ratio: 1 / 1;
  height: 100%;
  border-radius: 8px;
  background-color: #eeeeee;
}

.image-wrap img {
  width: 100%;
  height: 100%;
}

.profile-wrap {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: start;
}
h3.summary {
  color: var(--f1);
  font-size: 18px;
  font-weight: 500;
  margin-bottom: 12px;
}
pre.experience {
  color: var(--f2);
  font-size: 14px;
  font-weight: 400;
}
.tags {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
}
.tag {
  border-radius: 8px;
  border: 1px #bbb solid;
  color: var(--f2);
  font-size: 12px;
  font-weight: 400;
  padding: 4px 8px;
}
</style>