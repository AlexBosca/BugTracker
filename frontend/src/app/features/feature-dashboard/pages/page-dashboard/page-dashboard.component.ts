import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ProjectModel } from 'src/app/features/feature-projects/models/ProjectModel';
import { ProjectService } from 'src/app/features/feature-projects/services/project.service';

@Component({
  selector: 'app-page-dashboard',
  templateUrl: './page-dashboard.component.html',
  styleUrls: ['./page-dashboard.component.css']
})
export class PageDashboardComponent implements OnInit {

  projects!: ProjectModel[];
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
}
