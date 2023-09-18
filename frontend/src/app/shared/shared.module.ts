import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OrderByPipe } from './pipes/order-by.pipe';



@NgModule({
  declarations: [
    OrderByPipe
  ],
  imports: [
    CommonModule
  ],
  exports: [
    OrderByPipe
  ]
})
export class SharedModule { }
