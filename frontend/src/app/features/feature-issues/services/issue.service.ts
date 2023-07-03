import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable, of, throwError } from 'rxjs';
import { environment } from 'src/environments/environment';
import { AuthService } from '../../feature-auth/services/auth.service';
import { IssueModel } from '../models/IssueModel';
import { IssueRequestModel } from '../models/IssueRequestModel';
import { IssueStatusRequest } from '../models/IssueStatusRequest';

@Injectable({
  providedIn: 'root'
})
export class IssueService {
  private issuesUrl = `${environment.apiUrl}/issues`;

  constructor(
    private http: HttpClient,
    private authService: AuthService
    ) { }

  getIssues(): Observable<IssueModel[]> {
    return this.http.get<IssueModel[]>(this.issuesUrl)
      .pipe(
        catchError(this.handleError)
      );
  }

  getIssue(issueId: string): Observable<IssueModel> {
    return this.http.get<IssueModel>(`${this.issuesUrl}/${issueId}`);
  }

  createIssue(
    issueRequest: IssueRequestModel,
    projectKey: string
    ): Observable<void> {
    return this.http.post<void>(
      `${this.issuesUrl}/createOnProject/${projectKey}`,
      issueRequest
    );
  }

  changeIssueStatus(issueId: string, status: IssueStatusRequest): Observable<void> {
    let changeStateRequest = `${this.issuesUrl}/${issueId}/${status}`;
    let request = changeStateRequest;

    if(status === IssueStatusRequest.assignToDeveloper || status === IssueStatusRequest.closeByDeveloper) {
      let developerId = this.authService.getCurrentUser().userId;
      request = `${request}/${developerId}`;
    }

    return this.http.put<void>(
      request,
      null
    );
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
