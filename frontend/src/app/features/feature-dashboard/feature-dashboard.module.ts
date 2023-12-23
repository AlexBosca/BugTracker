import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { FeatureDashboardRoutingModule } from './feature-dashboard-routing.module';
import { PageDashboardComponent } from './pages/page-dashboard/page-dashboard.component';
import { ProjectsDropdownComponent } from './components/projects-dropdown/projects-dropdown.component';


@NgModule({
  declarations: [
    PageDashboardComponent,
    ProjectsDropdownComponent
  ],
  imports: [
    CommonModule,
    FeatureDashboardRoutingModule
  ]
})
export class FeatureDashboardModule { }
