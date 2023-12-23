import { Component, Input, OnInit } from '@angular/core';
import { ProjectModel } from 'src/app/features/feature-projects/models/ProjectModel';

@Component({
  selector: 'app-projects-dropdown',
  templateUrl: './projects-dropdown.component.html',
  styleUrls: ['./projects-dropdown.component.css']
})
export class ProjectsDropdownComponent implements OnInit {

  @Input() projects!: ProjectModel[];

  constructor() { }

  ngOnInit(): void {
  }

}
