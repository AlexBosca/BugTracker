import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { FeatureProjectsRoutingModule } from './feature-projects-routing.module';
import { PageProjectsComponent } from './pages/page-projects/page-projects.component';
import { ProjectCardComponent } from './components/project-card/project-card.component';
import { ProjectsGridComponent } from './components/projects-grid/projects-grid.component';
import { ProjectTableComponent } from './components/project-table/project-table.component';
import { ProjectFilterComponent } from './components/project-filter/project-filter.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { PageProjectsDetailsComponent } from './pages/page-projects-details/page-projects-details.component';
import { ProjectDetailsComponent } from './components/project-details/project-details.component';
import { IssueCardComponent } from '../feature-issues/components/issue-card/issue-card.component';
import { FeatureIssuesModule } from '../feature-issues/feature-issues.module';


@NgModule({
  declarations: [
    PageProjectsComponent,
    ProjectCardComponent,
    ProjectsGridComponent,
    ProjectTableComponent,
    ProjectFilterComponent,
    PageProjectsDetailsComponent,
    ProjectDetailsComponent
  ],
  imports: [
    CommonModule,
    FeatureProjectsRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    FeatureIssuesModule
  ]
})
export class FeatureProjectsModule { }
