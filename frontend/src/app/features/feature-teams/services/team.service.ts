import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { TeamModel } from '../models/TeamModel';
import { TeamRequestModel } from '../models/TeamRequestModel';

@Injectable({
  providedIn: 'root'
})
export class TeamService {
  private teamsUrl = `${environment.apiUrl}/teams`;

  constructor(private http: HttpClient) { }

  getTeams(): Observable<TeamModel[]> {
    return this.http.get<TeamModel[]>(this.teamsUrl);
  }

  getTeam(teamId: string): Observable<TeamModel> {
    return this.http.get<TeamModel>(`${this.teamsUrl}/${teamId}`);
  }

  createTeam(teamRequest: TeamRequestModel): Observable<void> {
    return this.http.post<void>(
      this.teamsUrl,
      teamRequest);
  }

  addColleagueInTeam(teamId: string, userId: string): Observable<void> {
    return this.http.put<void>(
      `${this.teamsUrl}/${teamId}/addColleague/${userId}`,
      null
    );
  }
}
