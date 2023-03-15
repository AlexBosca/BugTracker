import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { ProjectModel } from '../models/ProjectModel';
import { ProjectRequestModel } from '../models/ProjectRequestModel';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {
  private projectsUrl = `${environment.apiUrl}/projects`;

  constructor(private http: HttpClient) { }

  getProjects(): Observable<ProjectModel[]> {
    return this.http.get<ProjectModel[]>(this.projectsUrl);
  }

  getProject(projectId: string): Observable<ProjectModel> {
    return this.http.get<ProjectModel>(`${this.projectsUrl}/${projectId}`);
  }

  createProject(projectRequest: ProjectRequestModel): Observable<void> {
    return this.http.post<void>(
      this.projectsUrl,
      projectRequest
      );
  }

  addTeamToProjects(projectId: string, teamId: string) {
    return this.http.put<void>(
      `${this.projectsUrl}/${projectId}/addTeam/${teamId}`,
      null
    );
  }
}