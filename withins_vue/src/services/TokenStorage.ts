

export class TokenStorage {

  private readonly ACCESS_TOKEN_KEY = 'accessToken';
  private readonly REFRESH_TOKEN_KEY = 'refreshToken';

  async readAccessToken(): Promise<string | null> {
    return localStorage.getItem(this.ACCESS_TOKEN_KEY);
  }

  async readRefreshToken(): Promise<string | null> {
    return localStorage.getItem(this.REFRESH_TOKEN_KEY);
  }

  async saveTokens(accessToken: string, refreshToken?: string): Promise<void> {
    localStorage.setItem(this.ACCESS_TOKEN_KEY, accessToken);
    if (refreshToken) {
      localStorage.setItem(this.REFRESH_TOKEN_KEY, refreshToken);
    }
  }

  async clearTokens(): Promise<void> {
    localStorage.removeItem(this.ACCESS_TOKEN_KEY);
    localStorage.removeItem(this.REFRESH_TOKEN_KEY);
  }
}