import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PageIssuesComponent } from './pages/page-issues/page-issues.component';

const routes: Routes = [
  {
    path: '',
    component: PageIssuesComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class FeatureIssuesRoutingModule { }
