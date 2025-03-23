import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { MaterialModule } from "../shared/material.import";
import { TopNavComponent } from "./top-nav/top-nav.component";
import { LayoutComponent } from "./layout.component";

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    MaterialModule,
    LayoutComponent,
    TopNavComponent
  ],
  exports: [
    LayoutComponent
  ]
})
export class LayoutModule { }
