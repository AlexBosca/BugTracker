import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { IssueModel } from 'src/app/features/feature-issues/models/IssueModel';
import { IssueService } from 'src/app/features/feature-issues/services/issue.service';
import { ProjectModel } from 'src/app/features/feature-projects/models/ProjectModel';
import { ProjectService } from 'src/app/features/feature-projects/services/project.service';

@Component({
  selector: 'app-page-dashboard',
  templateUrl: './page-dashboard.component.html',
  styleUrls: ['./page-dashboard.component.css']
})
export class PageDashboardComponent implements OnInit {

  projects!: ProjectModel[];
  projectKey!: string;
  issues!: IssueModel[];
  error!: HttpErrorResponse;

  constructor(
    private issueService: IssueService,
    private projectService: ProjectService
    ) { }

  ngOnInit(): void {
    this.fetchProjects();

    if(!this.projectKey) {
      this.fetchAllIssues();
    }
  }

  fetchProjects(): void {
    this.projectService.getProjects()
        .subscribe({
          next: data => this.projects = data,
          error: error => this.error = error
        });
  }

  fetchIssuesOnProject(projectKey: string): void {
    this.projectService.getIssuesOnProject(projectKey)
        .subscribe({
          next: data => this.issues = data,
          error: error => this.error = error
        });
  }

  fetchAllIssues(): void {
    this.issueService.getIssues()
        .subscribe({
          next: data => this.issues = data,
          error: error => this.error = error
        });
  }

  onProjectChange(projectKey: string): void {
    this.fetchIssuesOnProject(projectKey);
  }
}
