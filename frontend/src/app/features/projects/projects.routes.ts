import { Routes } from "@angular/router";
import { ProjectsGridComponent } from "./projects-grid/projects-grid.component";
import { ProjectDetailsComponent } from "./project-details/project-details.component";
import { LayoutComponent } from "../../layout/layout.component";

export const routes: Routes = [
  {
    path: '',
    component: LayoutComponent,
    children: [
      { path: '', component: ProjectsGridComponent },
      { path: 'details', component: ProjectDetailsComponent }
    ]
  }
];
