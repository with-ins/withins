import axios, {AxiosError, AxiosRequestConfig} from "axios";
import {FetchResponse} from "@/api/FetchResponse";

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
      const response = await post('/api/v1/auth/refresh');
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
  },
}

export const userApi = {
  loadUser: async () : Promise<FetchResponse> => {
    return await get('/api/v1/auth/user');
  }
}

export const newsApi = {
  news: async (apiParams: any) : Promise<FetchResponse> => {
    return await get('/api/v1/news', {
      params: apiParams
    });
  }
}

export const signupApi = {
  existsUsername: async (username: string): Promise<boolean | null> => {
    const response = await get('http://localhost:8080/api/v1/signup/exists/username', {
      params: {username: username}
    });
    if (response.status === 200) {
      return response.data;
    }
    return null;
  },
  existsEmail: async (email: string): Promise<boolean | null> => {
    const response = await get('http://localhost:8080/api/v1/signup/exists/email', {
      params: {email: email}
    });
    return response.status === 200 ? response.data : null;
  },
  signup: async (username: string, password: string, email: string) : Promise<FetchResponse> => {
    return await post('http://localhost:8080/api/v1/signup', {
      username, password, email
    });
  }
}
const get = async <D = any> (url: string, data?: AxiosRequestConfig<D>) : Promise<FetchResponse> => {
  try {
    const response = await axios.get(url, data);
    return new FetchResponse(
        response.status,
        'SUCCESS',
        response.data
    );
  } catch (error: any) {
    const status : number = Number(error.response?.status) ?? 500;
    console.error(messageType[status]);
    return new FetchResponse(
        status,
        error.response.message,
        error.response.data,
    );
  }
}

const post = async <D = any>(url: string, data?: D, config?: AxiosRequestConfig<D>) : Promise<FetchResponse> => {
  try {
    const response = await axios.post(url, data, config);
    return new FetchResponse(
        response.status,
        'SUCCESS',
        response.data
    );
  } catch (error: any) {
    const status : number = Number(error.response?.status) ?? 500;
    console.error(messageType[status]);
    return new FetchResponse(
        status,
        error.response.message,
        error.response.data,
    );
  }
}

const messageType : Record<number, string> = {
  100: 'CONTINUE',
  101: 'SWITCHING_PROTOCOL',
  102: 'PROCESSING',
  103: 'EARLY_HINTS',
  200: 'OK',
  201: 'CREATED',
  202: 'ACCEPTED',
  203: 'NONAUTHORITATIVE_INFOMATION',
  204: 'NO_CONTENT',
  205: 'RESET_CONTENT',
  206: 'PARTIAL_CONTENT',
  207: 'MULTI_STATUS',
  208: 'MULTI_STATUS',
  226: 'IM_USED',
  300: 'MULTIPLE_CHOICE',
  301: 'MOVED_PERMANENTLY',
  302: 'FOUND',
  303: 'SEE_OTHER',
  304: 'NOT_MODIFIED',
  305: 'USE_PROXY',
  306: 'UNUSED',
  307: 'TEMPORARY_REDIRECT',
  308: 'PERMANENT_REDIRECT',
  400: 'BAD_REQUEST',
  401: 'UNAUTHORIZED',
  402: 'PAYMENT_REQUIRED',
  403: 'FORBIDDEN',
  404: 'NOT_FOUND',
  405: 'NOT_ACCEPTABLE',
  407: 'PROXY_AUTHENTICATION_REQUIRED',
  408: 'REQUEST_TIMEOUT',
  409: 'CONFLICT',
  410: 'GONE',
  411: 'LENGTH_REQUIRED',
  412: 'PRECONDITION_FAILED',
  413: 'PAYLOAD_TOO_LARGE',
  414: 'URI_TOO_LONG',
  415: 'UNSUPPORTED_MEDIA_TYPE',
  416: 'REQUESTED_RANGE_NOT_SATISFIABLE',
  417: 'EXPECTATION_FAILED',
  418: 'I\'M_A_TEAPOT',
  421: 'MISDIRECTED_REQUEST',
  422: 'UNPROCESSABLE_ENTITY',
  423: 'LOCKED',
  424: 'FAILED_DEPENDENCY',
  426: 'UPGRADE_REQUIRED',
  428: 'PRECONDITION_REQUIRED',
  429: 'TOO_MANY_REQUESTS',
  431: 'REQUEST_HEADER_FIELDS_TOO_LARGE',
  451: 'UNAVAILABLE_FOR_LEGAL_REASONS',
  500: 'SERVER_ERROR',
}


