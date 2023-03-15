import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PageTeamsComponent } from './pages/page-teams/page-teams.component';

const routes: Routes = [
  {
    path: '',
    component: PageTeamsComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class FeatureTeamsRoutingModule { }
