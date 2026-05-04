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
    return this.http.post<void>(`${environment.apiUrl}/projects`, projectRequest);
  }

  public getAllProjects(): Observable<Project[]> {
    return this.http.get<Project[]>(
      `${environment.apiUrl}/projects`,
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
    return this.http.get<Project>(`${environment.apiUrl}/projects/${projectKey}`);
  }

  public updateProject(projectKey: string, projectRequest: any): Observable<void> {
    return this.http.put<void>(`${environment.apiUrl}/projects/${projectKey}`, projectRequest);
  }

  public deleteProject(projectKey: string): Observable<void> {
    return this.http.delete<void>(`${environment.apiUrl}/projects/${projectKey}`);
  }

  public getUsersOnProject(projectKey: string): Observable<User[]> {
    return this.http.get<User[]>(`${environment.apiUrl}/projects/${projectKey}/users`);
  }

  public assignUsersToProject(projectKey: string, request: any): Observable<void> {
    return this.http.post<void>(`${environment.apiUrl}/projects/${projectKey}/users`, request);
  }

  public getUnassignedUsers(projectKey: string): Observable<User[]> {
    return this.http.get<User[]>(`${environment.apiUrl}/projects/${projectKey}/users/unassigned`);
  }

  public getProjectAvailableRoles(projectKey: string): Observable<string[]> {
    return this.http.get<string[]>(`${environment.apiUrl}/projects/${projectKey}/roles`);
  }
}
