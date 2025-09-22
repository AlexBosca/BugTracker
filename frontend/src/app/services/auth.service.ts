import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { Login } from '../models/login.model';
import { JwtResponse } from '../models/jwt-response.model';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly authenticationUrl = `${environment.apiUrl}/auth`;

  constructor(private router: Router, private http: HttpClient) { }

  public login(request: Login): Observable<JwtResponse> {
    return this.http.post<JwtResponse>(`${this.authenticationUrl}/login`, request, { withCredentials: true });
  }

  public register(request: any): Observable<any> {
    return this.http.post(`${this.authenticationUrl}/register`, request);
  }

  public refreshToken(): Observable<string> {
    return this.http.get<string>(`${this.authenticationUrl}/refresh`, { withCredentials: true, responseType: 'text' as 'json' })
      .pipe(
        map(response => {
          sessionStorage.setItem('accessToken', response);
          return response;
      }));
  }

  public logout(): void {
    this.http.post(`${this.authenticationUrl}/logout`, {}).subscribe({
      next: () => {
        this.clearAuthData();
        this.router.navigate(['/auth']);
      },
      error: (error) => {
        console.error('Logout failed', error);
      }
    });
    // this.router.navigate(['/auth']);
  }

  public isAuthenticated(): boolean {
    const isAuthenticated = (typeof window !== 'undefined') ? sessionStorage.getItem('isAuthenticated') : null;
    return isAuthenticated === 'true';
  }

  private clearAuthData(): void {
    sessionStorage.removeItem('accessToken');
    sessionStorage.removeItem('userId');
    sessionStorage.removeItem('email');
    sessionStorage.removeItem('roles');
    sessionStorage.setItem('isAuthenticated', 'false');
  }
}
