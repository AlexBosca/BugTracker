import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { ProjectModel } from '../../models/ProjectModel';
import { ProjectService } from '../../services/project.service';
import { IssueModel } from 'src/app/features/feature-issues/models/IssueModel';

@Component({
  selector: 'app-project-details',
  templateUrl: './project-details.component.html',
  styleUrls: ['./project-details.component.css']
})
export class ProjectDetailsComponent implements OnInit {
  private projectId!: string | null;
  project!: ProjectModel;
  issues!: IssueModel[];
  error!: HttpErrorResponse;

  constructor(
    private route: ActivatedRoute,
    private projectService: ProjectService
  ) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe(
      (params: ParamMap) => {
        this.projectId = params.get('id');
      }
    );

    this.fetchProject();
    this.fetchIssuesOnProject();
  }

  fetchProject(): void {
    if(this.projectId) {
      this.projectService.getProject(this.projectId)
        .subscribe({
          next: data => this.project = data,
          error: error => this.error = error
        });
    }
  }

  fetchIssuesOnProject(): void {
    if(this.projectId) {
      this.projectService.getIssuesOnProjects(this.projectId)
        .subscribe({
          next: data => this.issues = data,
          error: error => this.error = error
        });
    }
  }
}
