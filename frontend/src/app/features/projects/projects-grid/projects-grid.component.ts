import { Component } from '@angular/core';
import { MaterialModule } from '../../../shared/material.import';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SharedModule } from '../../../shared/shared.module';
import { ProjectService } from '../../../services/project.service';
import { Project } from '../../../models/project.model';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-projects-grid',
  imports: [MaterialModule, SharedModule, CommonModule, FormsModule, RouterModule],
  host: {
    '[style.--mat-form-field-container-vertical-padding]': '"0px"',
    '[style.--mat-form-field-container-height]': '"40px"'
  },
  template: `
    <div class="top-bar">
      <h2>Projects</h2>
      <!-- <div class="spacer"></div> -->
      <div class="search-bar">
        <mat-form-field appearance="outline">
          <mat-label>Search issues...</mat-label>
          <input matInput [(ngModel)]="searchQuery" (keyup)="onSearch(searchQuery)" placeholder="Search...">
          <mat-icon matSuffix>search</mat-icon>
        </mat-form-field>
      </div>
    </div>
    <mat-grid-list cols="4" rowHeight="1.5:1">
      @for (project of projects; track project) {
        <mat-grid-tile
          [colspan]="1"
          [rowspan]="1">
          <mat-card class="example-card" appearance="outlined"
            [routerLink]="['/projects/details']"
            [queryParams]="{ projectKey: project.projectKey }">
            <mat-card-header>
              <mat-card-title>{{project.name}}</mat-card-title>
            </mat-card-header>
            <mat-card-content>
              <p>{{project.description}}</p>
            </mat-card-content>
            <mat-card-footer class="example-card-footer">
              <mat-chip>Access Granted</mat-chip>
            </mat-card-footer>
          </mat-card>
        </mat-grid-tile>
      }
    </mat-grid-list>

  `,
  styleUrl: './projects-grid.component.css',
  styles: [`
    ::ng-deep .mat-mdc-card-header-text{
      width: 100%;
    }
    mat-form-field {
      width: 100%;
      margin: 0;
      height: 40px;
    }
    input {
      padding: 0.5rem;
    }
    h2 {
      margin: 1rem;
    }
    .margins {
      margin: 0.5rem;
    }
    .top-bar {
      margin: 1rem 0;
      display: flex;
      justify-content: start;
      align-items: center;
      padding: 0 1rem;
      gap: 3rem;
    }
    .spacer {
      flex-grow: 1;
    }
    .example-card {
      width: 20rem;
    }

    .example-card-footer {
      padding: 16px;
    }
  `]
})
export class ProjectsGridComponent {
  searchQuery = '';

  projects: Project[] = [];

  tiles = [
    {
      text: 'One',
      cols: 1,
      rows: 1,
      project: {
        projectKey: 'PRJ-1',
        name: 'Project 1',
        description: 'The Chihuahua is a Mexican breed of toy dog. It is named for the Mexican state of Chihuahua and is among the smallest of all dog breeds. It is usually kept as a companion animal or for showing.',
      }
    },
    {
      text: 'Two',
      cols: 1,
      rows: 1,
      project: {
        projectKey: 'PRJ-2',
        name: 'Project 2',
        description: 'The Chihuahua is a Mexican breed of toy dog. It is named for the Mexican state of Chihuahua and is among the smallest of all dog breeds. It is usually kept as a companion animal or for showing.',
      }
    },
    {
      text: 'Three',
      cols: 1,
      rows: 1,
      project: {
        projectKey: 'PRJ-3',
        name: 'Project 3',
        description: 'The Chihuahua is a Mexican breed of toy dog. It is named for the Mexican state of Chihuahua and is among the smallest of all dog breeds. It is usually kept as a companion animal or for showing.',
      }
    },
    {
      text: 'Four',
      cols: 1,
      rows: 1,
      project: {
        projectKey: 'PRJ-4',
        name: 'Project 4',
        description: 'The Chihuahua is a Mexican breed of toy dog. It is named for the Mexican state of Chihuahua and is among the smallest of all dog breeds. It is usually kept as a companion animal or for showing.',
      }
    },
    {
      text: 'Four',
      cols: 1,
      rows: 1,
      project: {
        projectKey: 'PRJ-5',
        name: 'Project 5',
        description: 'The Chihuahua is a Mexican breed of toy dog. It is named for the Mexican state of Chihuahua and is among the smallest of all dog breeds. It is usually kept as a companion animal or for showing.',
      }
    },
    {
      text: 'Four',
      cols: 1,
      rows: 1,
      project: {
        projectKey: 'PRJ-6',
        name: 'Project 6',
        description: 'The Chihuahua is a Mexican breed of toy dog. It is named for the Mexican state of Chihuahua and is among the smallest of all dog breeds. It is usually kept as a companion animal or for showing.',
      }
    },
    {
      text: 'Four',
      cols: 1,
      rows: 1,
      project: {
        projectKey: 'PRJ-7',
        name: 'Project 7',
        description: 'The Chihuahua is a Mexican breed of toy dog. It is named for the Mexican state of Chihuahua and is among the smallest of all dog breeds. It is usually kept as a companion animal or for showing.',
      }
    }
  ];

  longText = `The Chihuahua is a Mexican breed of toy dog. It is named for the
  Mexican state of Chihuahua and is among the smallest of all dog breeds. It is
  usually kept as a companion animal or for showing.`;

  constructor(
    readonly projectService: ProjectService
  ) {
    this.fetchProjects();
  }

  fetchProjects() {
    this.projectService.getAllProjects().subscribe({
      next: (projects) => {
        console.log('Projects:', projects);
        this.projects = projects;
      },
      error: (error) => {
        console.error('Error fetching projects:', error);
      }
    });
  }

  onSearch(query: string) {
    console.log('Search query:', query);
    // Implement search logic here
  }
}
