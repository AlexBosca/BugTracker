import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginFormComponent } from './components/login-form/login-form.component';
import { RegisterFormComponent } from './components/register-form/register-form.component';
import { ResetPasswordFormComponent } from './components/reset-password-form/reset-password-form.component';

const routes: Routes = [
  {
    path: 'login',
    component: LoginFormComponent  // TO DO: Replace with Auth Page
  },
  {
    path: 'register',
    component: RegisterFormComponent
  },
  {
    path: 'resetPassword',
    component: ResetPasswordFormComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class FeatureAuthRoutingModule { }
