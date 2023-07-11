import { Component, OnInit } from '@angular/core';
import { ProjectModel } from '../../models/ProjectModel';
import { HttpErrorResponse } from '@angular/common/http';
import { ProjectService } from '../../services/project.service';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { TeamService } from 'src/app/features/feature-teams/services/team.service';
import { TeamModel } from 'src/app/features/feature-teams/models/TeamModel';

@Component({
  selector: 'app-project-teams',
  templateUrl: './project-teams.component.html',
  styleUrls: ['./project-teams.component.css']
})
export class ProjectTeamsComponent implements OnInit {
  private projectKey!: string | null;
  teams!: TeamModel[];
  error!: HttpErrorResponse;

  constructor(
    private route: ActivatedRoute,
    private projectService: ProjectService
  ) { }

  ngOnInit(): void {
    this.route.parent!.paramMap.subscribe(
      (params: ParamMap) => {
        this.projectKey = params.get('id');
      }
    );

    this.fetchTeams();
  }

  fetchTeams(): void {
    if(this.projectKey) {
      this.projectService.getTeamsOnProject(this.projectKey)
        .subscribe({
          next: data => this.teams = data,
          error: error => this.error = error
        });
    }
  }
}
