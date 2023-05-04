import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PageProjectsDetailsComponent } from './pages/page-projects-details/page-projects-details.component';
import { PageProjectsComponent } from './pages/page-projects/page-projects.component';
import { ProjectsIssuesBoardComponent } from './components/projects-issues-board/projects-issues-board.component';

const routes: Routes = [
  {
    path: 'details/:id',
    component: PageProjectsDetailsComponent,
    children: [
      {
        path: 'board',
        component: ProjectsIssuesBoardComponent
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
