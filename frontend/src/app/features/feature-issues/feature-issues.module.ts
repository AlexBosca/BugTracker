import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';

import { FeatureIssuesRoutingModule } from './feature-issues-routing.module';
import { IssuesTableComponent } from './components/issues-table/issues-table.component';
import { PageIssuesComponent } from './pages/page-issues/page-issues.component';
import { IssuesFilterComponent } from './components/issues-filter/issues-filter.component';
import { IssueDetailsComponent } from './components/issue-details/issue-details.component';
import { PageIssueDetailsComponent } from './pages/page-issue-details/page-issue-details.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BasicAuthInterceptor } from 'src/app/core/interceptor/basic-auth.interceptor';


@NgModule({
  declarations: [
    IssuesTableComponent,
    PageIssuesComponent,
    IssuesFilterComponent,
    IssueDetailsComponent,
    PageIssueDetailsComponent
  ],
  imports: [
    CommonModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    FeatureIssuesRoutingModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS, useClass: BasicAuthInterceptor, multi: true
    }
  ]
})
export class FeatureIssuesModule { }
