import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PageProjectsDetailsComponent } from './pages/page-projects-details/page-projects-details.component';
import { PageProjectsComponent } from './pages/page-projects/page-projects.component';
import { ProjectsIssuesBoardComponent } from './components/projects-issues-board/projects-issues-board.component';
import { ProjectSummaryComponent } from './components/project-summary/project-summary.component';
import { ProjectsIssuesListComponent } from './components/projects-issues-list/projects-issues-list.component';

const routes: Routes = [
  {
    path: 'details/:id',
    component: PageProjectsDetailsComponent,
    children: [
      {
        path: '',
        redirectTo: 'board',
        pathMatch: 'full'
      },
      {
        path: 'board',
        component: ProjectsIssuesBoardComponent
      },
      {
        path: 'summary',
        component: ProjectSummaryComponent
      },
      {
        path: 'list',
        component: ProjectsIssuesListComponent
      }
    ]
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
