import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { IssueModel } from 'src/app/features/feature-issues/models/IssueModel';
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

  constructor(private projectService: ProjectService) { }

  ngOnInit(): void {
    this.fetchProjects();
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

  onProjectChange(projectKey: string): void {
    this.fetchIssuesOnProject(projectKey);
  }
}
