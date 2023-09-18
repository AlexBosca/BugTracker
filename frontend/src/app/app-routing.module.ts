import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './core/guards/auth.guard';
import { AdminGuard } from './core/guards/admin.guard';
import { AccessDeniedComponent } from './core/components/access-denied/access-denied.component';

const routes: Routes = [
  {
    path: 'issues',
    loadChildren: () => import('./features/feature-issues/feature-issues.module').then(m => m.FeatureIssuesModule),
    canActivate: [AuthGuard]
  },
  {
    path: 'teams',
    loadChildren: () => import('./features/feature-teams/feature-teams.module').then(m => m.FeatureTeamsModule),
    canActivate: [AuthGuard]
  },
  {
    path: 'projects',
    loadChildren: () => import('./features/feature-projects/feature-projects.module').then(m => m.FeatureProjectsModule),
    canActivate: [AuthGuard]
  },
  {
    path: 'users',
    loadChildren: () => import('./features/feature-users/feature-users.module').then(m => m.FeatureUsersModule),
    canActivate: [AuthGuard, AdminGuard]
  },
  {
    path: 'auth',
    loadChildren: () => import('./features/feature-auth/feature-auth.module').then(m => m.FeatureAuthModule)
  },
  {
    path: 'access-denied',
    component: AccessDeniedComponent
  },
  {
    path: '',
    redirectTo: '',
    pathMatch: 'full'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
