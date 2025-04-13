import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { Issue } from '../models/issue.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class IssueService {
  private readonly apiUrl = `${environment.apiUrl}/issues`;

  issues: Issue[] = [
    {
      issueId: 'SWD-123',
      title: 'Issue 1',
      assignee: 'User A',
      updatedBy: 'User B',
      projectName: 'Project A',
      projectKey: 'SWD',
      status: 'Open',
      priority: 'High',
      updatedAt: new Date('2024-07-01'),
      description: 'Description of Issue 1',
      createdAt: new Date('2024-06-01')
    },
    {
      issueId: 'SWD-456',
      title: 'Issue 2',
      assignee: 'User C',
      updatedBy: 'User D',
      projectName: 'Project B',
      projectKey: 'SWD',
      status: 'Closed',
      priority: 'Low',
      updatedAt: new Date('2024-08-01'),
      description: 'Description of Issue 2',
      createdAt: new Date('2024-07-01')
    }
  ];

  constructor(private http: HttpClient) {}

  public getAllIssues(): Observable<Issue[]> {
    // return of(this.issues);
    return this.http.get<Issue[]>(this.apiUrl);
  }

  public createIssue(issue: Issue): Observable<void> {
    this.issues.push(issue);
    return of(undefined);
  }

  public updateIssue(issueId: string, issue: Issue): Observable<void> {
    const index = this.issues.findIndex(i => i.issueId === issueId);
    if (index !== -1) {
      this.issues[index] = { ...this.issues[index], ...issue };
    }
    return of(undefined);
  }
}
