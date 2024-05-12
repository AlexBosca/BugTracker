import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { FeatureDashboardRoutingModule } from './feature-dashboard-routing.module';
import { PageDashboardComponent } from './pages/page-dashboard/page-dashboard.component';
import { ProjectsDropdownComponent } from './components/projects-dropdown/projects-dropdown.component';
import { IssuesStatusPiechartComponent } from './components/issues-status-piechart/issues-status-piechart.component';
import { IssuesPriorityPiechartComponent } from './components/issues-priority-piechart/issues-priority-piechart.component';
import { IssuesStatusByAssigneeComponent } from './components/issues-status-by-assignee/issues-status-by-assignee.component';


@NgModule({
  declarations: [
    PageDashboardComponent,
    ProjectsDropdownComponent,
    IssuesStatusPiechartComponent,
    IssuesPriorityPiechartComponent,
    IssuesStatusByAssigneeComponent
  ],
  imports: [
    CommonModule,
    FeatureDashboardRoutingModule
  ]
})
export class FeatureDashboardModule { }
