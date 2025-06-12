import axios, { AxiosError, AxiosRequestConfig } from "axios";

export class ApiClient {
  private isRefreshing = false;
  private failedQueue: Array<{
    resolve: (value: any) => void;
    reject: (error: any) => void;
  }> = [];

  constructor() {
    this._init();
  }

  private _init() {
    axios.defaults.withCredentials = true;
    axios.defaults.timeout = 10000;
    axios.defaults.headers.common['Content-Type'] = 'application/json';

    // 요청 인터셉터 설정
    axios.interceptors.request.use(
        async (config) => {
          return config;
        },
        (error) => {
          return Promise.reject(error);
        }
    );

    // 응답 인터셉터 설정
    axios.interceptors.response.use(
        response => response,
        async (error: AxiosError) => {
          const originalRequest = error.config as AxiosRequestConfig & { _retry?: boolean };

          // 토큰 갱신 요청이면 바로 에러 반환
          if (this.isTokenRefreshRequest(error.config)) {
            return Promise.reject(error);
          }

          // 로그아웃 요청이면 바로 에러 반환 (무한 루프 방지)
          if (this.isLogoutRequest(error.config)) {
            return Promise.reject(error);
          }

          if (error.response?.status === 401 && !originalRequest._retry) {
            if (this.isRefreshing) {
              // 이미 토큰 갱신 중이면 큐에 추가
              return new Promise((resolve, reject) => {
                this.failedQueue.push({ resolve, reject });
              }).then(() => {
                return axios(originalRequest);
              }).catch(err => {
                return Promise.reject(err);
              });
            }

            originalRequest._retry = true;
            this.isRefreshing = true;

            try {
              // 토큰 갱신 시도
              const success = await this.refreshToken();

              if (success) {
                this.processQueue(null);
                return axios(originalRequest);
              } else {
                // 토큰 갱신 실패
                this.processQueue(new Error('토큰 갱신 실패'));
                await this._handleTokenExpired();
                return Promise.reject(error);
              }
            } catch (refreshError) {
              this.processQueue(refreshError);
              await this._handleTokenExpired();
              return Promise.reject(error);
            } finally {
              // 🔥 중요: 반드시 상태 초기화
              this.isRefreshing = false;
            }
          }

          return Promise.reject(error);
        }
    );
  }

  private processQueue(error: any) {
    this.failedQueue.forEach(({ resolve, reject }) => {
      if (error) {
        reject(error);
      } else {
        resolve(null);
      }
    });
    this.failedQueue = [];
  }

  private isTokenRefreshRequest(config: any): boolean {
    if (!config?.url) return false;
    return config.url === '/api/v1/auth/refresh';
  }

  // 로그아웃 요청 체크
  private isLogoutRequest(config: any): boolean {
    if (!config?.url) return false;
    return config.url === '/api/v1/auth/logout';
  }

  private async _handleTokenExpired() {
    try {
      // 로그아웃 에러는 무시 (이미 토큰이 만료되었으므로)
      await authApi.logout();
    } catch (error) {
      console.warn('Logout failed, but proceeding with redirect:', error);
    }
  }

  private async refreshToken(): Promise<boolean> {
    try {
      const response = await axios.post('/api/v1/auth/refresh');
      if (response.status === 200) {
        console.log('Token refreshed successfully');
        return true;
      }
      return false;
    } catch (error) {
      console.error('Token refresh failed:', error);
      return false;
    }
  }
}

const apiClient = new ApiClient();
export { apiClient };
export default axios;

export const authApi = {
  formLogin: async (username: string, password: string) => {
    return await axios.post('/api/v1/login', {
      username: username,
      password: password
    });
  },
  logout: async () => {
    try {
      return await axios.post('/api/v1/auth/logout');
    } catch (error) {
      // 로그아웃 실패는 무시 (토큰이 이미 만료되었을 수 있음)
      console.warn('Logout request failed:', error);
      throw error;
    }
  }
}

export const userApi = {
  loadUser: async () => {
    return await axios.get('/api/v1/auth/user');
  }
}

export const newsApi = {
  news: async (apiParams: any) => {
    const response = await axios.get('/api/v1/news', {
      params: apiParams
    });
    return response.data;
  }
}