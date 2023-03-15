import { HttpErrorResponse } from '@angular/common/http';
import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { first } from 'rxjs';
import { UserModel } from '../../models/UserModel';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.css']
})
export class LoginFormComponent implements OnInit {

  loginForm!: FormGroup;
  loading: boolean = false;
  loginSubmitted: boolean = false;
  error!: HttpErrorResponse;
  returnUrl: string = '';

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private authService: AuthService,
    private formBuilder: FormBuilder
    ) {
      if(authService.isUserLoggedIn()) {
        this.router.navigate(['/']);
      }
    }

  ngOnInit(): void {
    this.loginForm = this.formBuilder.group({
      email: ['', Validators.required],
      password: ['', Validators.required]
    });
    
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
  }

  get form() {
    return this.loginForm.controls;
  }

  login(): void {
    this.loginSubmitted = true;

    if(!this.loginForm.valid) {
      return;
    }

    this.loading = true;

    this.authService.login(
      this.form['email'].value, 
      this.form['password'].value
      ).pipe(first())
        .subscribe({
          next: () => {
            this.router.navigate([this.returnUrl])
          },
          error: error => {
            this.error = error;
            this.loading = false;
          }
        });
  }
}
