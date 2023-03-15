import { Component, OnInit } from '@angular/core';
import { TeamModel } from '../../models/TeamModel';
import { TeamService } from '../../services/team.service';

@Component({
  templateUrl: './page-teams.component.html',
  styleUrls: ['./page-teams.component.css']
})
export class PageTeamsComponent implements OnInit {

  teams!: TeamModel[];

  constructor(private teamService: TeamService) { }

  ngOnInit(): void {
    this.fetchTeams();
  }

  fetchTeams(): void {
    this.teamService.getTeams()
        .subscribe({
          next: (data) => this.teams = data,
          error: (e) => console.error(e)
        });
  }
}
