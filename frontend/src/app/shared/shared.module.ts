import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MaterialModule } from './material.import';

@NgModule({
  imports: [CommonModule, MaterialModule],
  exports: [MaterialModule]
})
export class SharedModule {}
