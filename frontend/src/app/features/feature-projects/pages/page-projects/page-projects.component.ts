import { Component, OnInit } from '@angular/core';
import { ProjectModel } from '../../models/ProjectModel';
import { ProjectService } from '../../services/project.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  templateUrl: './page-projects.component.html',
  styleUrls: ['./page-projects.component.css']
})
export class PageProjectsComponent implements OnInit {

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

  handleFilteredData(filteredProjects: ProjectModel[]) {
    this.projects = filteredProjects;
    console.log(this.projects);
  }
}
