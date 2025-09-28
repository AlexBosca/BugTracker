import { Routes } from "@angular/router";
import { IssuesGridComponent } from "./issues-grid/issues-grid.component";
import { LayoutComponent } from "../../layout/layout.component";

export const routes: Routes = [
  {
    path: '',
    component: LayoutComponent,
    children: [
      { path: '', component: IssuesGridComponent }
    ]
  }
];
