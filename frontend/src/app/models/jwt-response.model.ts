export interface JwtResponse {
  accessToken: string;
  refreshToken: string;
  userId: string;
  email: string;
  roles: Set<string>;
}
