import { reactive } from 'vue';

// 요청 및 응답 유형을 정의
export interface RequestOptions {
  url: string;
  method: 'GET' | 'POST' | 'PUT' | 'DELETE' | 'PATCH';
  headers?: Record<string, string>;
  params?: Record<string, any>;
  data?: any;
}

export interface ResponseData<T = any> {
  data: T;
  status: number;
  statusText: string;
  headers: Record<string, string>;
  config: RequestOptions;
}

export interface ErrorData {
  message: string;
  response?: ResponseData;
  requestOptions: RequestOptions;
}

// 핸들러 유형을 정의
export interface Handler {
  next: (value: any) => Promise<any>;
  resolve: (value: any) => Promise<any>;
  reject: (error: any) => Promise<any>;
}

// 인터셉터 유형을 정의
export interface InterceptorOptions {
  onRequest?: (options: RequestOptions, handler: Handler) => Promise<any>;
  onResponse?: (response: ResponseData, handler: Handler) => Promise<any>;
  onError?: (error: ErrorData, handler: Handler) => Promise<any>;
}

// 인터셉터 클래스
export class FetchInterceptor {
  private onRequestCallback?: (options: RequestOptions, handler: Handler) => Promise<any>;
  private onResponseCallback?: (response: ResponseData, handler: Handler) => Promise<any>;
  private onErrorCallback?: (error: ErrorData, handler: Handler) => Promise<any>;

  constructor(options: InterceptorOptions) {
    this.onRequestCallback = options.onRequest;
    this.onResponseCallback = options.onResponse;
    this.onErrorCallback = options.onError;
  }

  async onRequest(options: RequestOptions, handler: Handler): Promise<any> {
    if (this.onRequestCallback) {
      return this.onRequestCallback(options, handler);
    }
    return handler.next(options);
  }

  async onResponse(response: ResponseData, handler: Handler): Promise<any> {
    if (this.onResponseCallback) {
      return this.onResponseCallback(response, handler);
    }
    return handler.next(response);
  }

  async onError(error: ErrorData, handler: Handler): Promise<any> {
    if (this.onErrorCallback) {
      return this.onErrorCallback(error, handler);
    }
    return handler.next(error);
  }
}

// 옵션 클래스
export class FetchOption {
  baseUrl: string;
  contentType: string;

  constructor(baseUrl: string, contentType: string = 'application/json') {
    this.baseUrl = baseUrl;
    this.contentType = contentType;
  }
}

// 메인 HTTP 클라이언트 클래스
export class FetchServer {
  private baseUrl: string;
  private contentType: string;
  private defaultHeaders: Record<string, string>;
  private interceptors: FetchInterceptor[] = [];

  constructor(options: FetchOption) {
    this.baseUrl = options.baseUrl;
    this.contentType = options.contentType;
    this.defaultHeaders = {
      'Content-Type': this.contentType,
      'Accept': 'application/json',
    };
  }

  // 인터셉터 추가 메서드
  addInterceptor(interceptor: FetchInterceptor): number {
    return this.interceptors.push(interceptor);
  }

  // 인터셉터 제거 메서드
  removeInterceptor(index: number): void {
    if (index >= 0 && index < this.interceptors.length) {
      this.interceptors.splice(index, 1);
    }
  }

  // 요청 전송 메서드
  async request<T = any>(options: RequestOptions): Promise<ResponseData<T>> {
    // 요청 옵션 준비
    const fullOptions: RequestOptions = {
      ...options,
      url: this.baseUrl + options.url,
      headers: {
        ...this.defaultHeaders,
        ...options.headers,
      },
    };

    // 요청 인터셉터 실행
    let processedOptions = await this.runRequestInterceptors(fullOptions);

    try {
      // 실제 요청 수행
      const response = await this.performRequest<T>(processedOptions);

      // 응답 인터셉터 실행
      return await this.runResponseInterceptors(response);
    } catch (error : Error | any) {
      // 에러 인터셉터 실행
      const errorData: ErrorData = {
        message: error.message || 'Request failed',
        response: error.response,
        requestOptions: processedOptions,
      };

      return await this.runErrorInterceptors(errorData);
    }
  }

  // 편의 메서드들
  async get<T = any>(url: string, params?: Record<string, any>, headers?: Record<string, string>): Promise<ResponseData<T>> {
    return this.request<T>({
      url,
      method: 'GET',
      params,
      headers,
    });
  }

  async post<T = any>(url: string, data?: any, headers?: Record<string, string>): Promise<ResponseData<T>> {
    return this.request<T>({
      url,
      method: 'POST',
      data,
      headers,
    });
  }

  async put<T = any>(url: string, data?: any, headers?: Record<string, string>): Promise<ResponseData<T>> {
    return this.request<T>({
      url,
      method: 'PUT',
      data,
      headers,
    });
  }

  async delete<T = any>(url: string, params?: Record<string, any>, headers?: Record<string, string>): Promise<ResponseData<T>> {
    return this.request<T>({
      url,
      method: 'DELETE',
      params,
      headers,
    });
  }

  // 인터셉터 실행 헬퍼 메서드들
  private async runRequestInterceptors(options: RequestOptions): Promise<RequestOptions> {
    let currentOptions = { ...options };

    for (const interceptor of this.interceptors) {
      const handler: Handler = {
        next: async (opts) => opts,
        resolve: async (response) => {
          throw new Error('Cannot resolve in request interceptor');
        },
        reject: async (error) => {
          throw error;
        },
      };

      currentOptions = await interceptor.onRequest(currentOptions, handler);
    }

    return currentOptions;
  }

  private async runResponseInterceptors<T = any>(response: ResponseData<T>): Promise<ResponseData<T>> {
    let currentResponse = { ...response };

    for (const interceptor of this.interceptors) {
      const handler: Handler = {
        next: async (res) => res,
        resolve: async (res) => res,
        reject: async (error) => {
          throw error;
        },
      };

      currentResponse = await interceptor.onResponse(currentResponse, handler);
    }

    return currentResponse;
  }

  private async runErrorInterceptors(error: ErrorData): Promise<any> {
    let currentError = { ...error };

    for (const interceptor of this.interceptors) {
      const handler: Handler = {
        next: async (err) => { throw err; },
        resolve: async (response) => response,
        reject: async (err) => { throw err; },
      };

      try {
        const result = await interceptor.onError(currentError, handler);
        if (result && !(result instanceof Error)) {
          return result; // 인터셉터가 에러를 해결한 경우
        }
        currentError = result;
      } catch (e : Error | any) {
        currentError = e;
      }
    }

    throw currentError;
  }

  // 실제 네트워크 요청 수행
  private async performRequest<T = any>(options: RequestOptions): Promise<ResponseData<T>> {
    const { url, method, headers, params, data } = options;

    // URL 파라미터 처리
    let fullUrl = url;
    if (params) {
      const queryParams = new URLSearchParams();
      Object.entries(params).forEach(([key, value]) => {
        queryParams.append(key, String(value));
      });
      fullUrl += `?${queryParams.toString()}`;
    }

    // fetch 옵션 준비
    const fetchOptions: RequestInit = {
      method,
      headers,
      credentials: 'include',
    };

    // body 추가 (GET 요청에는 body가 없음)
    if (method !== 'GET' && data !== undefined) {
      fetchOptions.body = headers?.['Content-Type'] === 'application/json'
        ? JSON.stringify(data)
        : data;
    }

    // 요청 실행
    const response = await fetch(fullUrl, fetchOptions);

    // 응답 처리
    let responseData: T;
    const contentType = response.headers.get('content-type');

    if (contentType?.includes('application/json')) {
      responseData = await response.json();
    } else {
      responseData = await response.text() as unknown as T;
    }

    // 응답 객체 생성
    const responseHeaders: Record<string, string> = {};
    response.headers.forEach((value, key) => {
      responseHeaders[key] = value;
    });

    const responseObj: ResponseData<T> = {
      data: responseData,
      status: response.status,
      statusText: response.statusText,
      headers: responseHeaders,
      config: options,
    };

    // 에러 응답 처리
    if (!response.ok) {
      const error: any = new Error(`Request failed with status ${response.status}`);
      error.response = responseObj;
      throw error;
    }

    return responseObj;
  }

  // 재시도 메서드
  async retry<T = any>(options: RequestOptions): Promise<ResponseData<T>> {
    return this.request<T>(options);
  }
}

// LocalStorage 유틸리티 클래스
export class LocalStorage {
  async generateDeviceId(): Promise<string> {
    let deviceId = localStorage.getItem('device_id');

    if (!deviceId) {
      deviceId = 'device_' + Math.random().toString(36).substring(2, 15);
      localStorage.setItem('device_id', deviceId);
    }

    return deviceId;
  }
}