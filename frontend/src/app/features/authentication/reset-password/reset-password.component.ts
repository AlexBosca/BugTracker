import { ChangeDetectionStrategy, Component } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { MaterialModule } from '../../../shared/material.import';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-reset-password',
  imports: [MaterialModule, FormsModule, ReactiveFormsModule, RouterModule],
  template: `
    <mat-card>
      <mat-card-header>
        <mat-card-title>Reset Password</mat-card-title>
      </mat-card-header>
      <mat-card-content>
        <form [formGroup]="form" (ngSubmit)="onSubmit()">
          <mat-form-field>
            <mat-label>Email</mat-label>
            <input matInput formControlName="email" type="email" required>
          </mat-form-field>
          <mat-form-field>
            <mat-label>New Password</mat-label>
            <input matInput formControlName="newPassword" type="password" required>
          </mat-form-field>
          <mat-form-field>
            <mat-label>Confirm New Password</mat-label>
            <input matInput formControlName="confirmNewPassword" type="password" required>
          </mat-form-field>
          <button mat-raised-button color="primary" type="submit">Reset Password</button>
        </form>
      </mat-card-content>
    </mat-card>
  `,
  styleUrl: './reset-password.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ResetPasswordComponent {
  form!: FormGroup;
  token!: string | null;

  constructor(
    private readonly formBuilder: FormBuilder,
    private readonly authService: AuthService,
    private readonly router: Router
  ) {
    this.form = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      newPassword: ['', [Validators.required, Validators.minLength(8), Validators.pattern('^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$')]],
      confirmNewPassword: ['', [Validators.required]]
    });
  }

  onSubmit(): void {
    if (this.form.valid) {
      const passwordUpdateData = this.form.value;
      this.token = this.router.routerState.snapshot.root.queryParamMap.get('token');

      if (this.token) {
        this.authService.passwordUpdate(this.token, passwordUpdateData.newPassword).subscribe({
          next: () => {
            // Handle successful password update (e.g., show a confirmation message)
            this.router.navigate(['/auth/login']);
          },
          error: (error) => {
            // Handle error (e.g., show an error message)
            console.error('Password update failed', error);
          }
        });
      } else {
        console.error('No token found in the URL');
      }

    } else {
      // Handle form validation errors (e.g., show an error message)
      console.error('Form is invalid');
    }
  }
}
