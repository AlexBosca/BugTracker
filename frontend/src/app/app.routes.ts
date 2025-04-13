import { Routes } from '@angular/router';
import { PageNotFoundComponent } from './layout/page-not-found/page-not-found.component';

export const routes: Routes = [
  // {
  //   path: '',
  //   loadChildren: () => import('./layout/layout.module').then(m => m.LayoutModule)
  // },
  {
    path: 'dashboard',
    loadChildren: () => import('./features/dashboard/dashboard.routes').then(m => m.routes)
  },
  {
    path: 'projects',
    loadChildren: () => import('./features/projects/projects.routes').then(m => m.routes)
  },
  {
    path: 'issues',
    loadChildren: () => import('./features/issues/issues.routes').then(m => m.routes)
  },
  {
    path: '**',
    component: PageNotFoundComponent
  }
];
