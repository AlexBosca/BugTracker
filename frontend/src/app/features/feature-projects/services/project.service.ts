import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, throwError } from 'rxjs';
import { environment } from 'src/environments/environment';
import { ProjectModel } from '../models/ProjectModel';
import { ProjectRequestModel } from '../models/ProjectRequestModel';
import { IssueModel } from '../../feature-issues/models/IssueModel';
import { TeamModel } from '../../feature-teams/models/TeamModel';
import { FilterCriteria } from '../../feature-issues/models/FilterCriteria';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {
  private projectsUrl = `${environment.apiUrl}/projects`;

  constructor(private http: HttpClient) { }

  getProjects(): Observable<ProjectModel[]> {
    return this.http.get<ProjectModel[]>(this.projectsUrl);
  }

  getFilteredProjects(filter: FilterCriteria): Observable<ProjectModel[]> {
    return this.http.post<ProjectModel[]>(
      `${this.projectsUrl}/filter`,
      filter
      )
      .pipe(
        catchError(this.handleError)
      );
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
