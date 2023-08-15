import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, map, Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { UserModel } from '../models/UserModel';
import { UserRegistrationModel } from '../models/UserRegistrationModel';
import { UserResetPasswordModel } from '../models/UserResetPasswordModel';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  CURRENT_USER_SESSION_ATTRIBUTE_NAME = 'currentUser';
  BASIC_AUTH_SESSION_ATTRIBUTE_NAME = 'basicAuth';
  public authenticationUrl = `${environment.apiUrl}/authentication`;
  public resetPasswordUrl = `${this.authenticationUrl}/resetPassword`;

  constructor(private http: HttpClient) { }

  public login(email: string, password: string): Observable<UserModel> {
    const headers = new HttpHeaders({ Authorization: this.generateAuthToken(email, password) });

    return this.http.get<UserModel>(
      this.authenticationUrl,
      { headers }
    ).pipe(map(userResponse => {
      sessionStorage.setItem(this.CURRENT_USER_SESSION_ATTRIBUTE_NAME, JSON.stringify(userResponse));
      let authToken = this.generateAuthToken(email, password);
      sessionStorage.setItem(this.BASIC_AUTH_SESSION_ATTRIBUTE_NAME, authToken);
      return userResponse;
    }));
  }

  public logout() {
    sessionStorage.removeItem(this.CURRENT_USER_SESSION_ATTRIBUTE_NAME);
    sessionStorage.removeItem(this.BASIC_AUTH_SESSION_ATTRIBUTE_NAME);
  }

  public register(registrationRequest: UserRegistrationModel): Observable<any> {
    return this.http.post(
      this.authenticationUrl,
      registrationRequest
    );
  }

  public resetPassword(resetPasswordRequest: UserResetPasswordModel): Observable<any> {
    let authToken = this.generateAuthToken(resetPasswordRequest.email, resetPasswordRequest.currentPassword);
    sessionStorage.setItem(this.BASIC_AUTH_SESSION_ATTRIBUTE_NAME, authToken);

    return this.http.put(
      this.resetPasswordUrl,
      resetPasswordRequest
    );
  }

  public generateAuthToken(email: string, password: string): string {
    const token = btoa(`${email}:${password}`);

    return `Basic ${token}`;
  }

  public isUserLoggedIn(): boolean {
    let currentUser = sessionStorage.getItem(this.CURRENT_USER_SESSION_ATTRIBUTE_NAME);

    return currentUser !== null;
  }

  public getCurrentUser(): UserModel {
    let currentUserAsString = sessionStorage.getItem(this.CURRENT_USER_SESSION_ATTRIBUTE_NAME);
    let currentUser = currentUserAsString ? JSON.parse(currentUserAsString) : null;

    return currentUser;
  }

  public getCurrentUserAuthToken(): string | null {
    let token = sessionStorage.getItem(this.BASIC_AUTH_SESSION_ATTRIBUTE_NAME);

    return token;
  }
}
