import { NgModule } from "@angular/core";
import { IssuesGridComponent } from "./issues-grid/issues-grid.component";
import { MaterialModule } from "../../shared/material.import";
import { SharedModule } from "../../shared/shared.module";

@NgModule({
  declarations: [],
  imports: [
    IssuesGridComponent,
    MaterialModule,
    SharedModule
  ],
  exports: [
    IssuesGridComponent
  ]
})
export class IssuesModule { }
