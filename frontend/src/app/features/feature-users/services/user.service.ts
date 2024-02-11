import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, throwError } from 'rxjs';
import { environment } from 'src/environments/environment';
import { UserModel } from '../../feature-auth/models/UserModel';
import { UserRegistrationModel } from '../../feature-auth/models/UserRegistrationModel';
import { FilterCriteria } from '../../feature-issues/models/FilterCriteria';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private usersUrl = `${environment.apiUrl}/users`;

  constructor(
    private http: HttpClient
  ) { }

  getUsers(): Observable<UserModel[]> {
    return this.http.get<UserModel[]>(this.usersUrl)
      .pipe(
        catchError(this.handleError)
      )
  }

  getFilteredUsers(filter: FilterCriteria): Observable<UserModel[]> {
    return this.http.post<UserModel[]>(
      `${this.usersUrl}/filter`,
      filter
      )
      .pipe(
        catchError(this.handleError)
      );
  }

  getUser(userId: string): Observable<UserModel> {
    return this.http.get<UserModel>(`${this.usersUrl}/${userId}`);
  }

  createUser(userRequest: UserRegistrationModel): Observable<void> {
    return this.http.post<void>(
      `${this.usersUrl}`,
      userRequest
    );
  }

  updateUser(userId: string, userRequest: UserRegistrationModel): Observable<void> {
    return this.http.put<void>(
      `${this.usersUrl}/${userId}`,
      userRequest
    );
  }

  enableAccount(userId: string): Observable<void> {
    let enableAccountRequest = `${this.usersUrl}/enable/${userId}`;
    return this.http.put<void>(
      enableAccountRequest,
      null
    );
  }

  disableAccount(userId: string): Observable<void> {
    let disableAccountRequest = `${this.usersUrl}/disable/${userId}`;
    return this.http.put<void>(
      disableAccountRequest,
      null
    );
  }

  unlockAccount(userId: string): Observable<void> {
    let unlockAccountRequest = `${this.usersUrl}/unlock/${userId}`;
    return this.http.put<void>(
      unlockAccountRequest,
      null
    );
  }

  deleteUser(userId: string): Observable<void> {
    let deleteUserRequest = `${this.usersUrl}/${userId}`;
    console.log(deleteUserRequest);
    return this.http.delete<void>(deleteUserRequest);
  }

  private handleError(error: HttpErrorResponse) {
    if (error.status === 0) {
      // A client-side or network error occurred. Handle it accordingly.
      console.error('An error occurred:', error.error);
    } else {
      // The backend returned an unsuccessful response code.
      // The response body may contain clues as to what went wrong.
      console.error(
        `Backend returned code ${error.status}, body was: `, error.error);
    }
    // Return an observable with a user-facing error message.
    return throwError(() => new Error('Something bad happened; please try again later.'));
  }
}
