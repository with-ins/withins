import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";
import HomeView from "../views/HomeView.vue";

const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    name: "home",
    component: () => import("../views/HomeView.vue"),
  },
  {
    path: '/login',
    component: () => import('../views/LoginView.vue'),
    children: [
      {
        path: '',
        name: 'login',
        component: () => import('../pages/login/LoginPage.vue'),
      },
      {
        path: 'success',
        name: 'login success',
        component: () => import('../pages/login/LoginSuccessPage.vue'),
      },
    ]
  },
  {
    path: "/news",
    component: () => import("../views/CenterNewsView.vue"),
    children: [
      {
        path: "",
        name: "centerNews",
        component: () => import("../pages/news/CenterNewsPage.vue"),
      }
    ]
  },
  {
    path: "/recruit",
    component: () => import("../views/RecruitmentView.vue"),
    children: [
      {
        path: "",
        name: "recruitmentList",
        component: () => import("../pages/recruitment/RecruitmentPage.vue"),
      },
      {
        path: ":recruitId",
        name: "recruitmentDetail",
        component: () => import("../pages/recruitment/RecruitmentDetailPage.vue"),
        props: true,
      }
    ]
  },
  {
    path: "/career",
    name: "careerProfile",
    component: () => import("../views/CareerProfileView.vue"),
    children: [
      {
        path: "",
        name: "careerList",
        component: () => import("../pages/career/CareerPage.vue"),
      }
    ]
  },
  // 존재하지 않는 경로에 대한 처리
  {
    path: "/:pathMatch(.*)*",
    name: "notFound",
    component: () => import("../views/error/NotFoundView.vue"),
  }
];
const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
