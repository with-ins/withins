<template>
  <!-- 세부 정보 컴포넌트 내용 -->
  <article>
    <div id="article-header">
      <h3>{{recruitDetail.welfareCenter.name}}</h3>
      <h1>{{recruitDetail.title}}</h1>
      <span>{{recruitDetail.getCreateAt()}}</span>
    </div>
    <div id="article-info">
      <div class="info-wrap">
        <div class="info-title">분류</div>
        <div class="info-content">{{recruitDetail.type}}</div>
      </div>
      <div class="info-wrap">
        <div class="info-title">지역</div>
        <div class="info-content">{{recruitDetail.welfareCenter.region}}</div>
      </div>
      <div class="info-wrap">
        <div class="info-title">모집일</div>
        <div class="info-content">{{recruitDetail.getDeadline()}}</div>
      </div>
    </div>
    <div id="article-content">
      <pre>{{recruitDetail.content}}</pre>
    </div>
    <div id="article-attached" v-if="recruitDetail.attachedFiles.length > 0">
      <a :href="file.filePath" :download="file.fileName" class="file-wrap" v-for="(file, index) in recruitDetail.attachedFiles" :key="index">
        <svg width="21" height="21" viewBox="0 0 21 21" fill="none" xmlns="http://www.w3.org/2000/svg">
          <path d="M5.55033 8.02429C5.89201 7.68284 6.44603 7.68268 6.78763 8.02429C7.12914 8.36589 7.12906 8.91992 6.78763 9.26159L4.31302 11.7371C2.94623 13.1039 2.9463 15.3203 4.31302 16.6871C5.67987 18.0538 7.89632 18.0539 9.2631 16.6871L11.7377 14.2117C12.0794 13.8702 12.6334 13.8701 12.975 14.2117C13.3165 14.5533 13.3164 15.1073 12.975 15.449L10.5004 17.9244C8.45021 19.9746 5.12599 19.9745 3.07572 17.9244C1.02558 15.8742 1.0255 12.55 3.07572 10.4997L5.55033 8.02429ZM13.4484 6.32128C13.7921 6.04129 14.2988 6.06086 14.6191 6.3811C14.9392 6.70134 14.9589 7.20811 14.6789 7.55175L14.6191 7.6184L7.61906 14.6184C7.27737 14.9601 6.72346 14.96 6.38175 14.6184C6.04004 14.2767 6.04004 13.7228 6.38175 13.3811L13.3818 6.3811L13.4484 6.32128ZM10.5004 3.07507C12.5506 1.02518 15.8749 1.02503 17.9251 3.07507C19.9751 5.12522 19.9749 8.44953 17.9251 10.4997L15.4496 12.9744C15.108 13.3159 14.554 13.3159 14.2123 12.9744C13.8706 12.6327 13.8706 12.0787 14.2123 11.7371L16.6878 9.26244C18.0542 7.89564 18.0543 5.67911 16.6878 4.31237C15.321 2.94576 13.1045 2.9459 11.7377 4.31237L9.26224 6.78698C8.92059 7.12854 8.36659 7.12853 8.02494 6.78698C7.68323 6.44527 7.68319 5.89133 8.02494 5.54968L10.5004 3.07507Z" fill="#45495E"/>
        </svg>
        {{file.fileName}}
      </a>
    </div>
  </article>
  <button id="back" @click="goBack">목록</button>
</template>

<script setup lang="ts">
import { useRouter, useRoute } from 'vue-router';
import {ApiServer} from "@/api/ApiServer";

const router = useRouter();
const route = useRoute();

const goBack = () => {
  if (route.state && route.state.returnPath) {
    // 이전 페이지 정보가 있으면 해당 페이지로 이동
    router.push({
      path: route.state.returnPath,
      query: route.state.returnQuery || {}
    });
  } else {
    // 이전 페이지 정보가 없으면 목록 페이지로 이동
    router.push('/recruit');
  }
};
const recruitId = route.params.recruitId;
const recruitDetail = ApiServer.mockRecruitDetail(`/recruit/${recruitId}`);
</script>

<style scoped>
article {
  margin-top: 60px;
}

/* Article Header */
#article-header {
  border-bottom: 1px #D9D9D9 solid;
  padding-bottom: 15px;
}
h1 {
  font-size: 21px;
  font-weight: 500;
  color: var(--f1);
  margin-bottom: 4px;
}
h3 {
  font-size: 16px;
  font-weight: 400;
  color: var(--f2);
  margin-bottom: 4px;
}
#article-header > span {
  font-size: 14px;
  color: var(--f3);
  font-weight: 400;
}

/* Article Info */
#article-info {
  padding: 20px;
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
}
.info-wrap {
  flex: 1;
  display: flex;
  height: 45px;
  align-items: center;
}
.info-title {
  width: 75px;
  height: 100%;
  background-color: #f8f8f8;
  display: flex;
  align-items: center;
  justify-content: center;

  border: 1px #eee solid;
  font-size: 14px;
}
.info-content {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: start;
  padding-left: 15px;
  color: var(--f1);
  font-weight: 500;
  font-size: 14px;
  border-top: 1px #eee solid;
  border-bottom: 1px #eee solid;
}

#article-info > .info-wrap:first-child .info-title {
  border-top-left-radius: 12px;
  border-bottom-left-radius: 12px;
}
#article-info > .info-wrap:last-child .info-content {
  border-top-right-radius: 12px;
  border-bottom-right-radius: 12px;
  border-right: 1px #eee solid;
}

/* Article Content */
#article-content {
  padding-left: 20px;
  padding-bottom: 30px;

  border-bottom: 1px #D9D9D9 solid;
}

/* Article Attach */
#article-attached {
  padding: 20px 10px;
  display: flex;
  flex-direction: column;
  gap: 10px;
  border-bottom: 1px #D9D9D9 solid;
}
.file-wrap {
  display: flex;
  align-items: center;
  gap: 10px;
}
.file-wrap:hover {
  color: var(--f2);
  text-decoration: underline;
}

/* Another */
#back {
  background-color: #FF9900;
  color: white;
  width: 80px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  font-weight: 500;
  border-radius: 8px;

  margin-top: 20px;
  float: right;
}
</style>