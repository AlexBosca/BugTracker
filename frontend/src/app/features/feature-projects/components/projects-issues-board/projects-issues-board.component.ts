import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { IssueModel } from 'src/app/features/feature-issues/models/IssueModel';
import { ProjectService } from '../../services/project.service';
import { ProjectModel } from '../../models/ProjectModel';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-projects-issues-board',
  templateUrl: './projects-issues-board.component.html',
  styleUrls: ['./projects-issues-board.component.css']
})
export class ProjectsIssuesBoardComponent implements OnInit {
  private projectId!: string | null;
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
        this.projectId = params.get('id');
      }
    );
    
    this.fetchIssuesOnProject();
  }

  fetchIssuesOnProject(): void {
    if(this.projectId) {
      console.log(this.projectId)
      this.projectService.getIssuesOnProjects(this.projectId)
        .subscribe({
          next: data => this.issues = data,
          error: error => this.error = error
        });
    }
  }
}
