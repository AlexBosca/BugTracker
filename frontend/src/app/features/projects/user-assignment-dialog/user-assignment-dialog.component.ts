import { Component, inject } from '@angular/core';
import { MaterialModule } from '../../../shared/material.import';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-user-assignment-dialog',
  imports: [MaterialModule, FormsModule, ReactiveFormsModule],
  template: `
    <h2 mat-dialog-title>Assign Users to Project</h2>

    <mat-dialog-content>
      <table mat-table [dataSource]="availableUsers" class="mat-elevation-z2" style="width: 100%;">
        <!-- User Column -->
        <ng-container matColumnDef="user">
          <th mat-header-cell *matHeaderCellDef>Name</th>
          <td mat-cell *matCellDef="let user">{{ user.firstName }} {{ user.lastName }}</td>
        </ng-container>

        <!-- Email Column -->
        <ng-container matColumnDef="email">
          <th mat-header-cell *matHeaderCellDef>Email</th>
          <td mat-cell *matCellDef="let user">{{ user.email }}</td>
        </ng-container>

        <!-- Role Select Column -->
        <ng-container matColumnDef="role">
          <th mat-header-cell *matHeaderCellDef>Role</th>
          <td mat-cell *matCellDef="let user">
            <mat-form-field appearance="outline" style="width: 100%;">
              <mat-select
                placeholder="Select Role"
                (selectionChange)="onRoleChange(user.userId, $event.value)">
                @for(role of availableRoles; track role) {
                  <mat-option [value]="role">{{ role }}</mat-option>
                }
              </mat-select>
            </mat-form-field>
          </td>
        </ng-container>

        <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
        <tr mat-row *matRowDef="let row; columns: displayedColumns;"></tr>
      </table>
    </mat-dialog-content>

    <mat-dialog-actions>
      <button mat-button mat-dialog-close>Cancel</button>
      <button mat-flat-button color="primary" (click)="assign()">Assign Users</button>
    </mat-dialog-actions>
  `,
  styleUrl: './user-assignment-dialog.component.css'
})
export class UserAssignmentDialogComponent {
  dialogRef = inject(MatDialogRef<UserAssignmentDialogComponent>);

  displayedColumns = ['user', 'email', 'role'];

  projectKey: string = inject(MAT_DIALOG_DATA).projectKey;
  availableUsers: any[] = inject(MAT_DIALOG_DATA).availableUsers;
  availableRoles: any[] = inject(MAT_DIALOG_DATA).availableRoles;
  selectedAssignments: { userId: string, roleName: string }[] = this.availableUsers.map(user => ({ userId: user.userId, roleName: '' }));

  onRoleChange(userId: string, roleName: string): void {
    const assignment = this.selectedAssignments.find(a => a.userId === userId);
    if (assignment) assignment.roleName = roleName;
    console.log(this.selectedAssignments);
  }

  assign(): void {
    const assignments: { userId: string, roleName: string }[] = [];

    this.selectedAssignments.forEach(assignment => {
      assignments.push(assignment);
    });

    console.log('Assignments:', assignments);

    this.dialogRef.close(assignments);
  }
}
