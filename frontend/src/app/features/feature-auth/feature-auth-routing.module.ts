import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginFormComponent } from './components/login-form/login-form.component';
import { RegisterFormComponent } from './components/register-form/register-form.component';
import { ResetPasswordFormComponent } from './components/reset-password-form/reset-password-form.component';
import { PageAuthenticationComponent } from './pages/page-authentication/page-authentication.component';

const routes: Routes = [
  {
    path: '',
    component: PageAuthenticationComponent,
    children: [
      {
        path: '',
        redirectTo: 'login',
        pathMatch: 'full'
      },
      {
        path: 'login',
        component: LoginFormComponent
      },
      {
        path: 'register',
        component: RegisterFormComponent
      },
      {
        path: 'resetPassword',
        component: ResetPasswordFormComponent
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class FeatureAuthRoutingModule { }
