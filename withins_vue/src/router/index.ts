import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";
import { authGuard } from "./guards";

const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    name: "home",
    component: () => import("../views/HomeView.vue"),
  },
  {
    path: '/login',
    component: () => import('../views/LoginView.vue'),
    meta: { requiresAuth: false },
    children: [
      {
        path: '',
        name: 'login',
        component: () => import('../pages/login/LoginPage.vue'),
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
    meta: { requiresAuth: true },
    children: [
      {
        path: "",
        name: "careerList",
        component: () => import("../pages/career/CareerPage.vue"),
      }
    ]
  },
  // {
  //   path: "/admin",
  //   name: "admin",
  //   component: () => import("../views/AdminView.vue"),
  //   meta: { requiresAuth: true, requiresAdmin: true },
  //   children: [
  //     {
  //       path: "",
  //       name: "adminDashboard",
  //       component: () => import("../pages/admin/AdminDashboard.vue"),
  //     }
  //   ]
  // },
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

router.beforeEach(authGuard);

export default router;
