import axios, { AxiosError, AxiosRequestConfig } from "axios";
import { TokenStorage } from "@/services/TokenStorage";

export class ApiClient {
  public storage = new TokenStorage();
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
    axios.defaults.baseURL = process.env.VUE_APP_API_URL || 'http://localhost:8080';
    axios.defaults.timeout = 10000; // 10초
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

        if (this.isTokenRefreshRequest(error.config)) {
          return Promise.reject(error);
        }

        if (error.response?.status === 401 && !originalRequest._retry) {

          if (this.isRefreshing) {
            // 이미 토큰 갱신 중이면 큐에 추가
            return new Promise((resolve, reject) => {
              this.failedQueue.push({ resolve, reject });
            }).then(token => axios(originalRequest))
              .catch(err => Promise.reject(err));
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
              this.processQueue(new Error('토큰 갱신 실패'))
              await this._handleTokenExpired();
              return Promise.reject(error);
            }
          } catch (refreshError) {
            this.processQueue(refreshError)
            await this._handleTokenExpired();
            return Promise.reject(error);
          }
        }
        // 에러 처리
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
    return config.url === '/auth/refresh';
  }

  private async _handleTokenExpired() {

    // 로그인 페이지로 리다이렉트
    if (window.location.pathname !== '/login') {
      window.location.href = '/login?expired';
    }
  }

  private async refreshToken() : Promise<boolean> {
    try {

      const response = await axios.post('/auth/refresh');

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
    return await axios.post('/api/login', {
      username: username,
      password: password
    });
  },

  logout: async () => {
    return await axios.post('/api/logout');
  }
}

export const userApi = {

  loadUser: async () => {
    return await axios.get('/api/user');
  }
}
export const newsApi = {

  news: async (apiParams : any) => {
    const response = await axios.get('/api/v1/news', {
      params: apiParams
    });

    return response.data;
  }
}


