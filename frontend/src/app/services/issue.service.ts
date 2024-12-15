import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Issue } from '../models/issue.model';

@Injectable({
  providedIn: 'root'
})
export class IssueService {

  constructor(readonly http: HttpClient) { }

  public createIssue(projectKey: string, issueRequest: any): Observable<void> {
    return this.http.post<void>(`http://localhost:8081/api/v1/bug-tracker/projects/${projectKey}/issues`, issueRequest);
  }

  public getAllIssues(projectKey: string): Observable<Issue[]> {
    return this.http.get<Issue[]>(`http://localhost:8081/api/v1/bug-tracker/projects/${projectKey}/issues`);
  }

  public getIssue(projectKey: string, issueId: string): Observable<Issue> {
    return this.http.get<Issue>(`http://localhost:8081/api/v1/bug-tracker/projects/${projectKey}/issues/${issueId}`);
  }

  public updateIssue(projectKey: string, issueId: string, issueRequest: any): Observable<void> {
    return this.http.put<void>(`http://localhost:8081/api/v1/bug-tracker/projects/${projectKey}/issues/${issueId}`, issueRequest);
  }

  public deleteIssue(projectKey: string, issueId: string): Observable<void> {
    return this.http.delete<void>(`http://localhost:8081/api/v1/bug-tracker/projects/${projectKey}/issues/${issueId}`);
  }
}
