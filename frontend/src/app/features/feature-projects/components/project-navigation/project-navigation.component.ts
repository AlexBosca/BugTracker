import { Component, Input, OnInit } from '@angular/core';
import { ProjectModel } from '../../models/ProjectModel';

@Component({
  selector: 'app-project-navigation',
  templateUrl: './project-navigation.component.html',
  styleUrls: ['./project-navigation.component.css']
})
export class ProjectNavigationComponent implements OnInit {

  @Input() project!: ProjectModel;

  constructor() { }

  ngOnInit(): void {
  }

}
