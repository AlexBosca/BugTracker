import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PageProjectsDetailsComponent } from './pages/page-projects-details/page-projects-details.component';
import { PageProjectsComponent } from './pages/page-projects/page-projects.component';

const routes: Routes = [
  {
    path: 'details/:id',
    component: PageProjectsDetailsComponent
  },
  {
    path: '',
    component: PageProjectsComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class FeatureProjectsRoutingModule { }
