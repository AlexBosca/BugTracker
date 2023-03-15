import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { FeatureAuthRoutingModule } from './feature-auth-routing.module';
import { LoginFormComponent } from './components/login-form/login-form.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RegisterFormComponent } from './components/register-form/register-form.component';


@NgModule({
  declarations: [
    LoginFormComponent,
    RegisterFormComponent
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
