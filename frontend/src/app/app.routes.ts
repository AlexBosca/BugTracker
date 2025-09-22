import { Routes } from '@angular/router';
import { PageNotFoundComponent } from './layout/page-not-found/page-not-found.component';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
  // {
  //   path: '',
  //   loadChildren: () => import('./layout/layout.module').then(m => m.LayoutModule)
  // },
  {
    path: 'dashboard',
    loadChildren: () => import('./features/dashboard/dashboard.routes').then(m => m.routes),
    canActivate: [authGuard]
  },
  {
    path: 'projects',
    loadChildren: () => import('./features/projects/projects.routes').then(m => m.routes),
    canActivate: [authGuard]
  },
  {
    path: 'issues',
    loadChildren: () => import('./features/issues/issues.routes').then(m => m.routes),
    canActivate: [authGuard]
  },
  {
    path: 'auth',
    loadChildren: () => import('./features/authentication/auth.routes').then(m => m.routes)
  },
  {
    path: '',
    redirectTo: '/dashboard',
    pathMatch: 'full'
  },
  {
    path: '**',
    component: PageNotFoundComponent
  }
];
