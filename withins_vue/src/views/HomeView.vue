<template>
  <div id="container">
    <section id="hero">
      <h1 class="animated">어떤 강사님을 찾으시나요?</h1>
      <h3 class="animated">프로그램에 딱 맞는 전문가를 찾아보세요</h3>
    </section>
    <section class="animated" id="search">
      <RouterLink to="/recruit">
        <div>
          <svg width="26" height="26" viewBox="0 0 26 26" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path fill-rule="evenodd" clip-rule="evenodd" d="M11.376 3.58301C15.6787 3.58345 19.1668 7.0722 19.167 11.375C19.1669 13.2564 18.4989 14.9818 17.3887 16.3281L22.1973 21.1367L22.248 21.1934C22.4885 21.4879 22.4719 21.9226 22.1973 22.1973C21.9226 22.4719 21.4879 22.4885 21.1934 22.248L21.1367 22.1973L16.3281 17.3887C14.9819 18.4987 13.2571 19.1658 11.376 19.166C7.07287 19.166 3.58318 15.6781 3.58301 11.375C3.58318 7.07193 7.07286 3.58301 11.376 3.58301ZM11.376 5.08301C7.90129 5.08301 5.08416 7.90036 5.08398 11.375C5.08416 14.8496 7.90129 17.666 11.376 17.666C14.8503 17.6656 17.6668 14.8494 17.667 11.375C17.6668 7.90063 14.8503 5.08345 11.376 5.08301Z" fill="#0071E3"/>
          </svg>
          <span>검색어를 입력해주세요</span>
        </div>
      </RouterLink>
    </section>
    <section id="intro">
      <div class="card animated d-1">
        <div class="card-content">
          <h3 class="text-gradient">복지관 소식</h3>
          <span>전국 각지의 복지관 소식과 프로그램 정보를 한눈에 확인할 수 있습니다. 최신 트렌드와 성공 사례를 공유하며 더 나은 복지 서비스를 위한 인사이트를 얻어보세요.</span>
        </div>
        <RouterLink to="/news">소식 보러가기</RouterLink>
      </div>
      <div class="card animated d-2">
        <div class="card-content">
          <h3 class="text-gradient">강사 구인구직</h3>
          <span>프로그램에 적합한 전문 강사를 쉽고 빠르게 찾을 수 있습니다. 복지 기관의 특성과 프로그램 목표에 맞는 맞춤형 강사 매칭 서비스를 제공합니다.</span>
        </div>
        <RouterLink to="/recruit">자세히 알아보기</RouterLink>
      </div>
      <div class="card animated d-3">
        <div class="card-content">
          <h3 class="text-gradient">강사 커리어</h3>
          <span>검증된 전문 강사들의 커리어와 전문 분야를 상세히 살펴볼 수 있습니다. 경력, 자격증, 전문성을 기반으로 프로그램에 최적화된 강사를 선택하세요.</span>
        </div>
        <RouterLink to="/career">강사 프로필 보기</RouterLink>
      </div>
    </section>
    <section id="empty"></section>
  </div>
</template>

<script lang="ts">
import { Options, Vue } from "vue-class-component";

@Options({
  mounted() {
    // 컴포넌트가 마운트된 후에 Observer 설정
    this.setupIntersectionObserver();
  },
  methods: {
    setupIntersectionObserver() {

      const observer = new IntersectionObserver((entries) => {
        entries.forEach((entry) => {
          if (entry.isIntersecting) {
            entry.target.classList.add('active');
          }
        });
      });

      const animations = document.querySelectorAll('.animated');
      for (const animation of animations) {
        observer.observe(animation);
      }

      // 컴포넌트 언마운트 시 정리를 위해 observer 저장
      this.observer = observer;
    }
  },
  beforeDestroy() {
    // 컴포넌트가 제거될 때 Observer 정리
    if (this.observer) {
      this.observer.disconnect();
    }
  }
})
export default class HomeView extends Vue {}
</script>

<style scoped>
#container {
  display: flex;
  flex-direction: column;
  align-items: center;
}

section {
  text-align: center;
}

/* Hero */
#hero {
  padding: 172px 0 75px 0;
}
#hero h1 {
  font-size: 40px;
  font-weight: 700;
  color: var(--f1);
  opacity: 0;
  transform: translateY(20px);
  transition: opacity 1s ease, transform 1s ease;
}
#hero h3 {
  font-size: 21px;
  font-weight: 300;
  color: var(--f3);
  opacity: 0;
  transform: translateY(20px);
  transition: opacity 1s ease, transform 1s ease;
  transition-delay: 0.2s;
}

/* Search */
#search {
  margin-bottom: 129px;
  opacity: 0;
  transform: translateY(50px);
  transition: opacity 1s ease, transform 1s ease;
}
#search a {
  height: 50px;
  width: 500px;
  border-radius: 100px;
  display: inline-block;
  position: relative;
  border: 1px solid transparent;
  background-image: linear-gradient(#fff, #fff), var(--main-color-gradient);
  background-origin: border-box;
  background-clip: content-box, border-box;
  box-shadow: 10px 10px 30px rgba(0, 0, 0, 0.06);
}

#search a > div {
  width: 100%;
  height: 100%;
  padding: 0 32px;
  display: flex;
  align-items: center;
  justify-content: start;
  gap: 26px;
}
#search span {
  font-size: 16px;
  font-weight: 300;
  color: var(--f3);
}

/* Card */
#intro {
  display: flex;
  gap: 40px;
  max-width: 1200px;
}
.card {
  flex: 1;
  padding: 30px;
  border-radius: 16px;
  box-shadow: 10px 10px 30px rgba(0, 0, 0, 0.06);
  border: 1px solid #eeeeee;
  min-height: 250px;

  display: flex;
  flex-direction: column;
  align-items: start;
  text-align: start;
  justify-content: space-between;
  gap: 21px;

  opacity: 0;
  transform: translateY(20px);
  transition: opacity 1s ease, transform 1s ease;
}
.card.d-1 {
  transition-delay: 0s;
}
.card.d-2 {
  transition-delay: 0.5s;
}
.card.d-3 {
  transition-delay: 1s;
}
.card-content h3 {
  font-size: 21px;
  font-weight: 600;
}
.card-content span {
  font-size: 16px;
  font-weight: 300;
  color: var(--f3);
  margin-top: 10px;
}
.card > a {
  padding: 10px 14px;
  border-radius: 100px;
  color: white;
  background-color: var(--main-color-1);
  font-size: 14px;
  font-weight: 400;
  margin-top: auto;
}

#empty {
  height: 100px;
}

/* animation */
.active {
  opacity: 1 !important;
  transform: translateY(0) !important;
}
</style>
