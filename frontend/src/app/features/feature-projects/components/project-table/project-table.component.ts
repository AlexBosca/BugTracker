import { HttpErrorResponse } from '@angular/common/http';
import { Component, Input } from '@angular/core';
import { ProjectModel } from '../../models/ProjectModel';
import { ProjectService } from '../../services/project.service';

@Component({
  selector: 'app-project-table',
  templateUrl: './project-table.component.html',
  styleUrls: ['./project-table.component.css']
})
export class ProjectTableComponent {

  @Input() projects!: ProjectModel[];
  error!: HttpErrorResponse;

  constructor() { }
}
