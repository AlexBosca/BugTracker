import { Component, OnInit } from '@angular/core';
import { ProjectModel } from '../../models/ProjectModel';
import { ProjectService } from '../../services/project.service';

@Component({
  templateUrl: './page-projects.component.html',
  styleUrls: ['./page-projects.component.css']
})
export class PageProjectsComponent implements OnInit {

  projects!: ProjectModel[];

  constructor(private projectService: ProjectService) { }

  ngOnInit(): void {
    // this.fetchProjects();
  }

  // fetchProjects(): void {
  //   this.projectService.getProjects()
  //       .subscribe({
  //         next: (data) => this.projects = data,
  //         error: (e) => console.error(e)
  //       });
  // }
}
