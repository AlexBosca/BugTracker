import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { ProjectModel } from 'src/app/features/feature-projects/models/ProjectModel';

@Component({
  selector: 'app-projects-dropdown',
  templateUrl: './projects-dropdown.component.html',
  styleUrls: ['./projects-dropdown.component.css']
})
export class ProjectsDropdownComponent implements OnInit {

  @Input() projects!: ProjectModel[];
  @Output() selectedProjectChange: EventEmitter<string> = new EventEmitter<string>();

  constructor() { }

  ngOnInit(): void {
  }

  onProjectChange(event: Event): void {
    const selectedProject = (event.target as HTMLSelectElement).value;
    this.selectedProjectChange.emit(selectedProject);
  } 
}
