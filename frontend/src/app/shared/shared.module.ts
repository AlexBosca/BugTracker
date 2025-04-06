import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FilterDialogComponent } from './filter-dialog/filter-dialog.component';

@NgModule({
  imports: [
    CommonModule,
    FilterDialogComponent
  ],
  exports: [
    FilterDialogComponent
  ]
})
export class SharedModule {}
