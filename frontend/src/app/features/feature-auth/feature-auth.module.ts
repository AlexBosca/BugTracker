import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { FeatureAuthRoutingModule } from './feature-auth-routing.module';
import { LoginFormComponent } from './components/login-form/login-form.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RegisterFormComponent } from './components/register-form/register-form.component';
import { ResetPasswordFormComponent } from './components/reset-password-form/reset-password-form.component';


@NgModule({
  declarations: [
    LoginFormComponent,
    RegisterFormComponent,
    ResetPasswordFormComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    FeatureAuthRoutingModule
  ],
  exports: [
    LoginFormComponent
  ]
})
export class FeatureAuthModule { }
