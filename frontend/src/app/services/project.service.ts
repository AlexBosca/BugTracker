import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Project } from '../models/project.model';
import { User } from '../models/user.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {
  private readonly projectIssuesApiUrl = `${environment.apiUrl}/projects`;

  constructor(readonly http: HttpClient) { }

  public createProject(projectRequest: any): Observable<void> {
    return this.http.post<void>(`https://localhost:8081/api/v1/bug-tracker/projects`, projectRequest);
  }

  public getAllProjects(): Observable<Project[]> {
    return this.http.get<Project[]>(
      `https://localhost:8081/api/v1/bug-tracker/projects`,
      {
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json',
          'Access-Control-Allow-Origin': '*'
        }
      }
    );
    // return this.http.get<Project[]>(`http://localhost:8081/api/v1/bug-tracker/projects`);
  }

  public getProject(projectKey: string): Observable<Project> {
    return this.http.get<Project>(`https://localhost:8081/api/v1/bug-tracker/projects/${projectKey}`);
  }

  public updateProject(projectKey: string, projectRequest: any): Observable<void> {
    return this.http.put<void>(`https://localhost:8081/api/v1/bug-tracker/projects/${projectKey}`, projectRequest);
  }

  public deleteProject(projectKey: string): Observable<void> {
    return this.http.delete<void>(`https://localhost:8081/api/v1/bug-tracker/projects/${projectKey}`);
  }

  public getUsersOnProject(projectKey: string): Observable<User[]> {
    return this.http.get<User[]>(`https://localhost:8081/api/v1/bug-tracker/projects/${projectKey}/users`);
  }

  public assignUsersToProject(projectKey: string, request: any): Observable<void> {
    return this.http.post<void>(`https://localhost:8081/api/v1/bug-tracker/projects/${projectKey}/users`, request);
  }

  public getUnassignedUsers(projectKey: string): Observable<User[]> {
    return this.http.get<User[]>(`https://localhost:8081/api/v1/bug-tracker/projects/${projectKey}/users/unassigned`);
  }

  public getProjectAvailableRoles(projectKey: string): Observable<string[]> {
    return this.http.get<string[]>(`https://localhost:8081/api/v1/bug-tracker/projects/${projectKey}/roles`);
  }
}
