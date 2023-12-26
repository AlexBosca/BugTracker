import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { FeatureDashboardRoutingModule } from './feature-dashboard-routing.module';
import { PageDashboardComponent } from './pages/page-dashboard/page-dashboard.component';
import { ProjectsDropdownComponent } from './components/projects-dropdown/projects-dropdown.component';
import { IssuesStatusPiechartComponent } from './components/issues-status-piechart/issues-status-piechart.component';


@NgModule({
  declarations: [
    PageDashboardComponent,
    ProjectsDropdownComponent,
    IssuesStatusPiechartComponent
  ],
  imports: [
    CommonModule,
    FeatureDashboardRoutingModule
  ]
})
export class FeatureDashboardModule { }
