import { Component, Input, OnInit } from '@angular/core';
import { ProjectModel } from '../../models/ProjectModel';
import { IssueModel } from 'src/app/features/feature-issues/models/IssueModel';
import { HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { ProjectService } from '../../services/project.service';
import { UserSlimModel } from 'src/app/features/feature-auth/models/UserSlimModel';

@Component({
  selector: 'app-project-people',
  templateUrl: './project-people.component.html',
  styleUrls: ['./project-people.component.css']
})
export class ProjectPeopleComponent implements OnInit {
  projectKey!: string;
  project!: ProjectModel;
  usersOnProject: UserSlimModel[] = [];
  error!: HttpErrorResponse;

  constructor(
    private route: ActivatedRoute,
    private projectService: ProjectService
  ) { }

  ngOnInit(): void {
    this.route.parent!.paramMap.subscribe(
      (params: ParamMap) => {
        this.projectKey = params.get('id')!;
      }
    );

    this.fetchProject();

    console.log(this.project.assignedUsers);
  }

  fetchProject(): void {
    if(this.projectKey) {
      this.projectService.getProject(this.projectKey)
          .subscribe({
            next: data => {
              this.project = data;

              if(this.project.assignedUsers && this.project.projectManager) {
                this.usersOnProject = [...data.assignedUsers];
                this.usersOnProject.push(data.projectManager);
              }
            },
            error: error => this.error = error
          });
    }
  }
}
