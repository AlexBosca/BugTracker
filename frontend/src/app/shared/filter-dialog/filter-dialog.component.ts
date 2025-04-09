import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { MaterialModule } from '../material.import';
import { FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-filter-dialog',
  imports: [MaterialModule, FormsModule, ReactiveFormsModule],
  template: `
    <h2 mat-dialog-title>Filter</h2>
    <mat-dialog-content class="mat-typography">
      <mat-form-field>
        <mat-label>Project Name</mat-label>
        <mat-select [formControl]="projectNamesAndKeys" multiple>
          @for (project of projectNamesAndKeyList; track project) {
            <mat-option [value]="project.projectKey">{{project.projectName}}</mat-option>
          }
        </mat-select>
      </mat-form-field>

      <mat-form-field>
        <mat-label>Priority</mat-label>
        <mat-select [formControl]="priorities" multiple>
          @for (priority of priorityList; track priority) {
            <mat-option [value]="priority">{{priority}}</mat-option>
          }
        </mat-select>
      </mat-form-field>

      <mat-form-field>
        <mat-label>Assignee</mat-label>
        <mat-select [formControl]="assignees" multiple>
          @for (assignee of assigneeList; track assignee) {
            <mat-option [value]="assignee">{{assignee}}</mat-option>
          }
        </mat-select>
      </mat-form-field>
    </mat-dialog-content>
    <mat-dialog-actions>
      <button mat-button mat-dialog-close>Cancel</button>
      <button mat-button (click)="applyFilter()" cdkFocusInitial>Install</button>
    </mat-dialog-actions>
  `,
  styleUrl: './filter-dialog.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class FilterDialogComponent {
  private readonly dialogRef = inject(MatDialogRef<FilterDialogComponent>);

  activeFilters = inject(MAT_DIALOG_DATA).activeFilters;

  projectNamesAndKeys = new FormControl(
    this.activeFilters.projectNamesAndKeys.map((item: { projectKey: string; projectName: string }) => item.projectKey)
  );

  projectNamesAndKeyList: {
    projectKey: string;
    projectName: string;
  }[] = inject(MAT_DIALOG_DATA).projectNamesAndKeys

  priorities = new FormControl(this.activeFilters.priorities);
  priorityList: string[] = inject(MAT_DIALOG_DATA).priorityList;

  assignees = new FormControl(this.activeFilters.assignees);
  assigneeList: string[] = inject(MAT_DIALOG_DATA).assigneeList;

  isSelected(option: string): boolean {
    return this.activeFilters.projectNames.value.includes(option);
  }

  applyFilter(): void {
    const selectedNames = this.projectNamesAndKeys.value ?? [];

    this.activeFilters.projectNamesAndKeys = selectedNames
      .map((projectKey: string) => this.projectNamesAndKeyList.find(project => project.projectKey === projectKey))
      .filter((p: unknown): p is { projectKey: string; projectName: string } => !!p);
    this.activeFilters.priorities = this.priorities.value;
    this.activeFilters.assignees = this.assignees.value;

    this.dialogRef.close(this.activeFilters);
  }
}
