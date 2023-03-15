import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { IssueDetailsComponent } from './components/issue-details/issue-details.component';
import { PageIssueDetailsComponent } from './pages/page-issue-details/page-issue-details.component';
import { PageIssuesComponent } from './pages/page-issues/page-issues.component';

const routes: Routes = [
  {
    path: 'details/:id',
    component: PageIssueDetailsComponent,
  },
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
