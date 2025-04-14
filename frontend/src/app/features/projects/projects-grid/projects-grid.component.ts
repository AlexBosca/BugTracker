import { Component } from '@angular/core';
import { MaterialModule } from '../../../shared/material.import';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SharedModule } from '../../../shared/shared.module';

@Component({
  selector: 'app-projects-grid',
  imports: [MaterialModule, SharedModule, CommonModule, FormsModule],
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
    <mat-grid-list cols="4" rowHeight="100px">
      @for (tile of tiles; track tile) {
        <mat-grid-tile
          [colspan]="tile.cols"
          [rowspan]="tile.rows">
          <mat-card appearance="outlined">
            <mat-card-content>Simple card</mat-card-content>
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
      max-width: 400px;
    }

    .example-card-footer {
      padding: 16px;
    }
  `]
})
export class ProjectsGridComponent {
  searchQuery = '';

  tiles = [
    {text: 'One', cols: 1, rows: 1, color: 'lightblue'},
    {text: 'Two', cols: 1, rows: 1, color: 'lightgreen'},
    {text: 'Three', cols: 1, rows: 1, color: 'lightpink'},
    {text: 'Four', cols: 1, rows: 1, color: '#DDBDF1'},
    {text: 'Four', cols: 1, rows: 1, color: '#DDBDF1'},
    {text: 'Four', cols: 1, rows: 1, color: '#DDBDF1'},
    {text: 'Four', cols: 1, rows: 1, color: '#DDBDF1'}
  ];

  onSearch(query: string) {
    console.log('Search query:', query);
    // Implement search logic here
  }
}
