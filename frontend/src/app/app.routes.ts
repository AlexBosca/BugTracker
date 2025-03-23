import { Routes } from '@angular/router';
import { PageNotFoundComponent } from './layout/page-not-found/page-not-found.component';

export const routes: Routes = [
  // {
  //   path: '',
  //   loadChildren: () => import('./layout/layout.module').then(m => m.LayoutModule)
  // },
  {
    path: 'dashboard',
    loadChildren: () => import('./features/dashboard/dashboard.module').then(m => m.DashboardModule)
  },
  {
    path: 'projects',
    loadChildren: () => import('./features/projects/projects.module').then(m => m.ProjectsModule)
  },
  {
    path: 'issues',
    loadChildren: () => import('./features/issues/issues.module').then(m => m.IssuesModule)
  },
  {
    path: '**',
    component: PageNotFoundComponent
  }
];
