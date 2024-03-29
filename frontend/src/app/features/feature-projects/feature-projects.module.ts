import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { FeatureProjectsRoutingModule } from './feature-projects-routing.module';
import { PageProjectsComponent } from './pages/page-projects/page-projects.component';
import { ProjectCardComponent } from './components/project-card/project-card.component';
import { ProjectsGridComponent } from './components/projects-grid/projects-grid.component';
import { ProjectTableComponent } from './components/project-table/project-table.component';
import { ProjectFilterComponent } from './components/project-filter/project-filter.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgChartsModule } from 'ng2-charts';
import { PageProjectsDetailsComponent } from './pages/page-projects-details/page-projects-details.component';
import { ProjectDetailsComponent } from './components/project-details/project-details.component';
import { FeatureIssuesModule } from '../feature-issues/feature-issues.module';
import { ProjectsIssuesBoardComponent } from './components/projects-issues-board/projects-issues-board.component';
import { ProjectNavigationComponent } from './components/project-navigation/project-navigation.component';
import { ProjectSummaryComponent } from './components/project-summary/project-summary.component';
import { ProjectsIssuesListComponent } from './components/projects-issues-list/projects-issues-list.component';
import { ProjectCalendarComponent } from './components/project-calendar/project-calendar.component';
import { ProjectTeamsComponent } from './components/project-teams/project-teams.component';


@NgModule({
  declarations: [
    PageProjectsComponent,
    ProjectCardComponent,
    ProjectsGridComponent,
    ProjectTableComponent,
    ProjectFilterComponent,
    PageProjectsDetailsComponent,
    ProjectDetailsComponent,
    ProjectsIssuesBoardComponent,
    ProjectNavigationComponent,
    ProjectSummaryComponent,
    ProjectsIssuesListComponent,
    ProjectCalendarComponent,
    ProjectTeamsComponent
  ],
  imports: [
    CommonModule,
    FeatureProjectsRoutingModule,
    FormsModule,
    NgChartsModule,
    ReactiveFormsModule,
    FeatureIssuesModule
  ]
})
export class FeatureProjectsModule { }
