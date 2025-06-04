import { NavigationGuardNext, RouteLocationNormalized } from 'vue-router'
import { useUserStore } from '@/stores/UserStore'

export const authGuard = async (
  to: RouteLocationNormalized,
  from: RouteLocationNormalized,
  next: NavigationGuardNext
) => {
  const userStore = useUserStore()
  
  // 초기 인증 상태 로드 (아직 로드되지 않은 경우에만)
  if (!userStore.isInitialized) {
    await userStore.initialize()
  }

  // 인증이 필요한 라우트에 대한 처리
  if (to.meta.requiresAuth && userStore.isAnonymous) {
    return next({ path: '/login', query: { redirect: to.fullPath } })
  }

  // 관리자 권한이 필요한 라우트에 대한 처리
  if (to.meta.requiresAdmin && !userStore.isAdmin) {
    return next({ path: '/' })
  }

  // 이미 로그인한 사용자의 로그인 페이지 접근 제한
  if (to.path === '/login' && !userStore.isAnonymous) {
    return next({ path: '/' })
  }

  next()
} 