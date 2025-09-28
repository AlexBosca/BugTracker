import { ChangeDetectionStrategy, Component } from '@angular/core';
import { MaterialModule } from "../../../shared/material.import";
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../../services/auth.service';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-login',
  imports: [MaterialModule, FormsModule, ReactiveFormsModule, RouterModule],
  template: `
  <mat-card>
    <mat-card-header>
      <mat-card-title>Login</mat-card-title>
    </mat-card-header>
    <mat-card-content>
      <form [formGroup]="form" (ngSubmit)="onSubmit()">
        <mat-form-field>
          <mat-label>Email</mat-label>
          <input matInput formControlName="username" type="email" required>
        </mat-form-field>
        <mat-form-field>
          <mat-label>Password</mat-label>
          <input matInput formControlName="password" type="password" required>
        </mat-form-field>
        <button mat-raised-button color="primary" type="submit">Login</button>
      </form>
    </mat-card-content>
    <mat-card-actions>
      <a mat-button routerLink="/auth/register">Don't have an account? Register</a>
      <a mat-button routerLink="/auth/forgot-password">Forgot Password?</a>
    </mat-card-actions>
  </mat-card>
  `,
  styleUrl: './login.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class LoginComponent {
  form!: FormGroup;

  constructor(
    private readonly formBuilder: FormBuilder,
    private readonly authService: AuthService,
    private readonly router: Router,
    private readonly route: ActivatedRoute
  ) {
    this.form = this.formBuilder.group({
      username: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8), Validators.pattern('^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$')]]
    });
  }


  onSubmit() {
    if (this.form.valid) {
      const loginData = this.form.value;
      // Call the AuthService to handle login
      this.authService.login(loginData).subscribe({
        next: (response) => {
          // Handle successful login
          sessionStorage.setItem('accessToken', response.accessToken);
          sessionStorage.setItem('isAuthenticated', 'true');
          sessionStorage.setItem('userId', response.userId);
          sessionStorage.setItem('email', response.email);
          sessionStorage.setItem('roles', JSON.stringify(response.roles));
          // Navigate to the return URL or default route
          const returnUrl = this.route.snapshot.queryParamMap.get('returnUrl') || '/dashboard';
          this.router.navigateByUrl(returnUrl);
        },
        error: (error) => {
          // Handle login error
          console.error('Login failed', error);
          // Optionally, you can show an error message to the user
          alert('Login failed. Please check your credentials and try again.');
        }
      });
    } else {
      console.error('Form is invalid');
    }
  }

  public isAuthenticated(): boolean {
    return this.authService.isAuthenticated();
  }
}
