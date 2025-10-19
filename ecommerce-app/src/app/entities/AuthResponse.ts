export interface AuthResponse {
  accessToken: string;
  refreshToken: string;
  tokenType: string;     // "Bearer"
  expiresInMs: number;
}
