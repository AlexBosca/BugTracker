import { ChangeDetectionStrategy, Component } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { MaterialModule } from '../../../shared/material.import';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-forgot-password',
  imports: [MaterialModule, FormsModule, ReactiveFormsModule, RouterModule],
  template: `
    <mat-card>
      <mat-card-header>
        <mat-card-title>Forgot Password</mat-card-title>
      </mat-card-header>
      <mat-card-content>
        <form [formGroup]="form" (ngSubmit)="onSubmit()">
          <mat-form-field>
            <mat-label>Email</mat-label>
            <input matInput formControlName="email" type="email" required>
          </mat-form-field>
          <button mat-raised-button color="primary" type="submit">Send Reset Link</button>
        </form>
      </mat-card-content>

      <mat-card-actions>
        <a mat-button routerLink="/auth/login">Back to Login</a>
      </mat-card-actions>
    </mat-card>
  `,
  styleUrl: './forgot-password.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ForgotPasswordComponent {
  form!: FormGroup;

  constructor(
    private readonly formBuilder: FormBuilder,
    private readonly authService: AuthService,
    private readonly router: Router
  ) {
    this.form = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]]
    });
  }

  onSubmit() {
    if (this.form.valid) {
      const email = this.form.value.email;
      this.authService.passwordReset(email).subscribe({
        next: () => {
          // Handle successful password reset request (e.g., show a confirmation message)
          this.router.navigate(['/auth/login']);
        },
        error: (error) => {
          // Handle error (e.g., show an error message)
          console.error('Password reset request failed', error);
        }
      });
    } else {
      // Handle form validation errors (e.g., show an error message)
      console.error('Form is invalid');
    }
  }
}
