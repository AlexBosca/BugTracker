import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

  public createUser(userRequest: any): Observable<void> {
    return this.http.post<void>(`https://localhost:8081/api/v1/bug-tracker/users`, userRequest);
  }

  public getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(`https://localhost:8081/api/v1/bug-tracker/users`);
  }

  public getUser(userId: string): Observable<User> {
    return this.http.get<User>(`https://localhost:8081/api/v1/bug-tracker/users/${userId}`);
  }

  public updateUser(userId: string, userRequest: any): Observable<void> {
    return this.http.put<void>(`https://localhost:8081/api/v1/bug-tracker/users/${userId}`, userRequest);
  }

  public deleteUser(userId: string): Observable<void> {
    return this.http.delete<void>(`https://localhost:8081/api/v1/bug-tracker/users/${userId}`);
  }
}
