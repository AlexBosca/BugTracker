import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { FeatureIssuesRoutingModule } from './feature-issues-routing.module';
import { IssuesTableComponent } from './components/issues-table/issues-table.component';
import { PageIssuesComponent } from './pages/page-issues/page-issues.component';
import { IssuesFilterComponent } from './components/issues-filter/issues-filter.component';


@NgModule({
  declarations: [
    IssuesTableComponent,
    PageIssuesComponent,
    IssuesFilterComponent
  ],
  imports: [
    CommonModule,
    FeatureIssuesRoutingModule
  ]
})
export class FeatureIssuesModule { }
