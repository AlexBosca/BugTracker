import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { ProjectModel } from '../models/ProjectModel';
import { ProjectRequestModel } from '../models/ProjectRequestModel';
import { IssueModel } from '../../feature-issues/models/IssueModel';
import { TeamModel } from '../../feature-teams/models/TeamModel';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {
  private projectsUrl = `${environment.apiUrl}/projects`;

  constructor(private http: HttpClient) { }

  getProjects(): Observable<ProjectModel[]> {
    return this.http.get<ProjectModel[]>(this.projectsUrl);
  }

  getProject(projectKey: string): Observable<ProjectModel> {
    return this.http.get<ProjectModel>(`${this.projectsUrl}/${projectKey}`);
  }

  createProject(projectRequest: ProjectRequestModel): Observable<void> {
    return this.http.post<void>(
      this.projectsUrl,
      projectRequest
      );
  }

  addTeamToProjects(projectKey: string, teamId: string): Observable<void> {
    return this.http.put<void>(
      `${this.projectsUrl}/${projectKey}/addTeam/${teamId}`,
      null
    );
  }

  getIssuesOnProject(projectKey: string): Observable<IssueModel[]> {
    return this.http.get<IssueModel[]>(`${this.projectsUrl}/${projectKey}/issues`);
  }

  getTeamsOnProject(projectKey: string): Observable<TeamModel[]> {
    return this.http.get<TeamModel[]>(`${this.projectsUrl}/${projectKey}/teams`);
  }
}
