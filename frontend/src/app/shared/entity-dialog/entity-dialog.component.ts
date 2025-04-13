/* eslint-disable @typescript-eslint/no-explicit-any */
import { ChangeDetectionStrategy, Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MaterialModule } from '../material.import';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { provideNativeDateAdapter } from '@angular/material/core';

@Component({
  selector: 'app-entity-dialog',
  imports: [MaterialModule, FormsModule, ReactiveFormsModule],
  providers: [provideNativeDateAdapter()],
  template: `
    <h2 mat-dialog-title>{{data.title}}</h2>
    <form [formGroup]="form" (submit)="onSubmit()">
      <mat-dialog-content class="mat-typography" disableClose>
        @for (field of data.fields; track field.name) {
          <mat-form-field>
            <mat-label>{{ field.label }}</mat-label>

            @switch (field.type) {
              @case ('text') {
                <input matInput [formControlName]="field.name" required/>
              }

              @case ('text-area') {
                <textarea matInput [formControlName]="field.name" required></textarea>
              }

              @case ('select') {
                <mat-select [formControlName]="field.name" required>
                  @for (option of field.options ?? []; track option) {
                    <mat-option [value]="option">{{option}}</mat-option>
                  }
                </mat-select>
              }

              @case ('date') {
                <ng-container>
                  <input matInput [matDatepicker]="picker" [formControlName]="field.name" required/>
                  <mat-datepicker-toggle matIconSuffix [for]="picker"></mat-datepicker-toggle>
                  <mat-datepicker #picker></mat-datepicker>
                </ng-container>
              }
            }

          </mat-form-field>
        }
      </mat-dialog-content>
      <mat-dialog-actions>
        <button mat-button (click)="cancel()">Cancel</button>
        <button mat-button type="submit">{{data.type}}</button>
      </mat-dialog-actions>
    </form>
  `,
  styleUrl: './entity-dialog.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class EntityDialogComponent<T> implements OnInit {
  mode: 'create' | 'edit' = 'create';
  form!: FormGroup;

  constructor(
    private readonly fb: FormBuilder,
    private readonly dialogRef: MatDialogRef<EntityDialogComponent<T>>,
    @Inject(MAT_DIALOG_DATA) public data: {
      title: string,
      fields: {
        name: string,
        label: string,
        type: string,
        options?: string[]
      } [],
      entityData?: Record<string, any>,
      type: 'Create' | 'Edit',
    }
  ) { }

  ngOnInit(): void {
    const controls: Record<string, any> = {};
    for (const field of this.data.fields) {
      const initialValue = this.data.entityData?.[field.name] ?? '';
      controls[field.name] = [initialValue];
    }

    this.form = this.fb.group(controls);
  }

  projectList = [
    {
      projectKey: 'SWA',
      projectName: 'Project A'
    },
    {
      projectKey: 'SWB',
      projectName: 'Project B'
    },
    {
      projectKey: 'SWC',
      projectName: 'Project C'
    },
    {
      projectKey: 'SWD',
      projectName: 'Project D'
    }
  ];

  onSubmit() {
    if(!this.form.value) {
      console.error('Form is invalid');
      this.dialogRef.close();
    } else if(this.form.valid) {
      const updatedEntity = {
        ...this.data.entityData,
        ...this.form.value
      };

      this.dialogRef.close(updatedEntity);
    }
  }

  cancel() {
    this.dialogRef.close();
  }
}
