import { ChangeDetectionStrategy, Component, Input, OnInit } from '@angular/core';
import { ProjectModel } from '../../models/ProjectModel';

@Component({
  selector: 'app-projects-grid',
  templateUrl: './projects-grid.component.html',
  styleUrls: ['./projects-grid.component.css']
})
export class ProjectsGridComponent implements OnInit {

  @Input() projects!: ProjectModel[];

  constructor() { }

  ngOnInit(): void {
  }

}
