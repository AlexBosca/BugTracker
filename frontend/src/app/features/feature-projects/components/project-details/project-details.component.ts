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
  private projectKey!: string | null;
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
        this.projectKey = params.get('id');
      }
    );

    this.fetchProject();
  }

  fetchProject(): void {
    if(this.projectKey) {
      this.projectService.getProject(this.projectKey)
        .subscribe({
          next: data => this.project = data,
          error: error => this.error = error
        });
    }
  }
}
