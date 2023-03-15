import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { UserRegistrationModel } from '../../models/UserRegistrationModel';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-register-form',
  templateUrl: './register-form.component.html',
  styleUrls: ['./register-form.component.css']
})
export class RegisterFormComponent implements OnInit {

  registrationForm!: FormGroup;
  submitted: boolean = false;
  loading: boolean = false;
  error!: HttpErrorResponse;

  constructor(
    private router: Router,
    private authService: AuthService,
    private formBuilder: FormBuilder,
  ) { }

  ngOnInit(): void {
    this.registrationForm = this.formBuilder.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', Validators.required],
      password: ['', Validators.required],
      role: ['V', Validators.required]
    });
  }

  get form() {
    return this.registrationForm.controls;
  }

  register(): void {
    this.submitted = true;

    if(!this.registrationForm.valid) {
      return;
    }

    this.loading = true;

    this.authService.register({
      firstName: this.form['firstName'].value,
      lastName: this.form['lastName'].value,
      email: this.form['email'].value,
      password: this.form['password'].value,
      role: this.form['role'].value,
    }).subscribe({
      next: () => {
        this.router.navigate(['/login']);
      },
      error: error => {
        this.error = error;
        this.loading = false;
      }
    });
  }
}
