import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { UserRegistrationModel } from '../../models/UserRegistrationModel';
import { AuthService } from '../../services/auth.service';
import { Modal } from 'bootstrap';

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
  registerModal!: Modal;

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
      role: ['', Validators.required]
    });

    this.registerModal = new Modal(document.getElementById('registerModal') as HTMLElement);

    this.registerModal.show();
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
        this.router.navigate(
          ['/login'],
          {
            state: {
              data: 'Confirm your email address'
            }
          }
        );

        this.closeModal();
      },
      error: error => {
        this.error = error;
        this.loading = false;
      }
    });
  }

  closeModal(): void {
    this.registerModal.hide();
  }
}
