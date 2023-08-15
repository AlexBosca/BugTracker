import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-reset-password-form',
  templateUrl: './reset-password-form.component.html',
  styleUrls: ['./reset-password-form.component.css']
})
export class ResetPasswordFormComponent implements OnInit {

  resetPasswordForm!: FormGroup;
  loading: boolean = false;
  submitted: boolean = false;
  error!: HttpErrorResponse;

  constructor(
    private router: Router,
    private authService: AuthService,
    private formBuilder: FormBuilder
  ) { }

  ngOnInit(): void {
    this.resetPasswordForm = this.formBuilder.group({
      email: ['', Validators.required],
      currentPassword: ['', Validators.required],
      newPassword: ['', Validators.required],
      newPasswordRepeated: ['', Validators.required]
    });
  }

  get form() {
    return this.resetPasswordForm.controls;
  }

  resetPassword(): void {
    this.submitted = true;

    if(!this.resetPasswordForm.valid) {
      return ;
    }

    this.loading = true;

    this.authService.resetPassword({
      email: this.form['email'].value,
      currentPassword: this.form['currentPassword'].value,
      newPassword: this.form['newPassword'].value,
      newPasswordRepeated: this.form['newPasswordRepeated'].value
    }).subscribe({
      next: () => {
        this.router.navigate(
          ['/login'],
          {
            state: {
              data: 'Password successfully reset'
            }
          }
        );
      },
      error: error => {
        this.error = error;
        this.loading = false;
      }
    });
  }
}
