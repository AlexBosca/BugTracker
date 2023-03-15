import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { FeatureProjectsRoutingModule } from './feature-projects-routing.module';
import { PageProjectsComponent } from './pages/page-projects/page-projects.component';
import { ProjectCardComponent } from './components/project-card/project-card.component';
import { ProjectsGridComponent } from './components/projects-grid/projects-grid.component';
import { ProjectTableComponent } from './components/project-table/project-table.component';
import { ProjectFilterComponent } from './components/project-filter/project-filter.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';


@NgModule({
  declarations: [
    PageProjectsComponent,
    ProjectCardComponent,
    ProjectsGridComponent,
    ProjectTableComponent,
    ProjectFilterComponent
  ],
  imports: [
    CommonModule,
    FeatureProjectsRoutingModule,
    FormsModule,
    ReactiveFormsModule
  ]
})
export class FeatureProjectsModule { }
