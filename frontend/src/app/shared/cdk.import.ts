import { NgModule } from '@angular/core';
import { CdkDrag, CdkDropList, CdkDropListGroup } from '@angular/cdk/drag-drop';

export const CDK_IMPORTS = [
  CdkDrag,
  CdkDropList,
  CdkDropListGroup
];

@NgModule({
  imports: CDK_IMPORTS,
  exports: CDK_IMPORTS
})
export class CdkModule { }
