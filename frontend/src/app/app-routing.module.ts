import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './core/guards/auth.guard';

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
    path: 'auth',
    loadChildren: () => import('./features/feature-auth/feature-auth.module').then(m => m.FeatureAuthModule)
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
