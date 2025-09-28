import { ChangeDetectionStrategy, Component } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { MaterialModule } from '../../../shared/material.import';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-register',
  imports: [MaterialModule, FormsModule, ReactiveFormsModule, RouterModule],
  template: `
  <mat-card>
    <mat-card-header>
      <mat-card-title>Register</mat-card-title>
    </mat-card-header>
    <mat-card-content>
      <form [formGroup]="form" (ngSubmit)="onSubmit()">
        <mat-form-field>
          <mat-label>First Name</mat-label>
          <input matInput formControlName="firstName" type="text" required>
        </mat-form-field>
        <mat-form-field>
          <mat-label>Last Name</mat-label>
          <input matInput formControlName="lastName" type="text" required>
        </mat-form-field>
        <mat-form-field>
          <mat-label>Email</mat-label>
          <input matInput formControlName="email" type="email" required>
        </mat-form-field>
        <mat-form-field>
          <mat-label>Password</mat-label>
          <input matInput formControlName="password" type="password" required>
        </mat-form-field>
        <mat-form-field>
          <mat-label>Confirm Password</mat-label>
          <input matInput formControlName="confirmPassword" type="password" required>
        </mat-form-field>
        <button mat-raised-button color="primary" type="submit">Register</button>
      </form>
    </mat-card-content>
    <mat-card-actions>
      <a mat-button routerLink="/auth/login">Already have an account? Login</a>
    </mat-card-actions>
  `,
  styleUrl: './register.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class RegisterComponent {
  form!: FormGroup;

  constructor(
    private readonly formBuilder: FormBuilder,
    private readonly authService: AuthService,
    private readonly router: Router
  ) {
    this.form = this.formBuilder.group({
      firstName: ['', [Validators.required, Validators.minLength(2)]],
      lastName: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8), Validators.pattern('^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$')]],
      confirmPassword: ['', [Validators.required]]
    });
  }

  onSubmit() {
    if (this.form.valid) {
      const registerData = this.form.value;
      this.authService.register(registerData).subscribe({
        next: () => {
          // Handle successful registration
          this.router.navigate(['/auth/login']);
        },
        error: (error) => {
          // Handle registration error
          console.error('Registration failed', error);
          alert('Registration failed. Please try again.');
        }
      });
    } else {
      console.error('Form is invalid');
    }
  }
}
