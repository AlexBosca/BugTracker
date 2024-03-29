import { Component, OnInit } from '@angular/core';
import { ProjectModel } from '../../models/ProjectModel';
import { IssueModel } from 'src/app/features/feature-issues/models/IssueModel';
import { HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { ProjectService } from '../../services/project.service';

@Component({
  selector: 'app-projects-issues-list',
  templateUrl: './projects-issues-list.component.html',
  styleUrls: ['./projects-issues-list.component.css']
})
export class ProjectsIssuesListComponent implements OnInit {
  private projectKey!: string | null;
  project!: ProjectModel;
  issues: IssueModel[] = [];
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

    this.fetchIssuesOnProject();
  }

  fetchIssuesOnProject(): void {
    if(this.projectKey) {
      this.projectService.getIssuesOnProject(this.projectKey)
          .subscribe({
            next: data => this.issues = data,
            error: error => this.error = error
          });
    }
  }
}
