import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Project } from '../models/project.model';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {

  constructor(readonly http: HttpClient) { }

  public createProject(projectRequest: any): Observable<void> {
    return this.http.post<void>(`http://localhost:8081/api/v1/bug-tracker/projects`, projectRequest);
  }

  public getAllProjects(): Observable<Project[]> {
    return this.http.get<Project[]>(`http://localhost:8081/api/v1/bug-tracker/projects`);
  }

  public getProject(projectKey: string): Observable<Project> {
    return this.http.get<Project>(`http://localhost:8081/api/v1/bug-tracker/projects/${projectKey}`);
  }

  public updateProject(projectKey: string, projectRequest: any): Observable<void> {
    return this.http.put<void>(`http://localhost:8081/api/v1/bug-tracker/projects/${projectKey}`, projectRequest);
  }

  public deleteProject(projectKey: string): Observable<void> {
    return this.http.delete<void>(`http://localhost:8081/api/v1/bug-tracker/projects/${projectKey}`);
  }
}
