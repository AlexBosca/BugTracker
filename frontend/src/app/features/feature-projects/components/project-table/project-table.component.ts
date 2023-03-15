import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ProjectModel } from '../../models/ProjectModel';
import { ProjectService } from '../../services/project.service';

@Component({
  selector: 'app-project-table',
  templateUrl: './project-table.component.html',
  styleUrls: ['./project-table.component.css']
})
export class ProjectTableComponent implements OnInit {

  projects!: ProjectModel[];
  error!: HttpErrorResponse;

  constructor(
    private projectService: ProjectService
    ) { }

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
