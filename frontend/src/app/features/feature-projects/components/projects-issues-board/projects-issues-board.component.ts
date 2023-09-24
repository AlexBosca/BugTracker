import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { IssueModel } from 'src/app/features/feature-issues/models/IssueModel';
import { ProjectService } from '../../services/project.service';
import { ProjectModel } from '../../models/ProjectModel';
import { HttpErrorResponse } from '@angular/common/http';
import { StatusCategory } from 'src/app/features/feature-issues/models/StatusCategory';

@Component({
  selector: 'app-projects-issues-board',
  templateUrl: './projects-issues-board.component.html',
  styleUrls: ['./projects-issues-board.component.css']
})
export class ProjectsIssuesBoardComponent implements OnInit {
  private projectKey!: string | null;
  project!: ProjectModel;
  issues!: IssueModel[];
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

  getToDoIssues(issues: IssueModel[]): IssueModel[] {
    return issues.filter(issue => StatusCategory.TO_DO.includes(issue.status))
  }

  getInProgressIssues(issues: IssueModel[]): IssueModel[] {
    return issues.filter(issue => StatusCategory.IN_PROGRESS.includes(issue.status))
  }

  getDoneIssues(issues: IssueModel[]): IssueModel[] {
    return issues.filter(issue => StatusCategory.DONE.includes(issue.status))
  }
}
