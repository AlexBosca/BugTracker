import { NgModule } from '@angular/core';
import { MatBadgeModule } from '@angular/material/badge';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatChipsModule } from '@angular/material/chips';
import { MatDividerModule } from '@angular/material/divider';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatListModule } from '@angular/material/list';
import { MatMenuModule } from '@angular/material/menu';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatDialogModule } from '@angular/material/dialog';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';

export const MATERIAL_IMPORTS = [
  MatCardModule,
  MatChipsModule,
  MatToolbarModule,
  MatButtonModule,
  MatIconModule,
  MatFormFieldModule,
  MatInputModule,
  MatMenuModule,
  MatBadgeModule,
  MatDividerModule,
  MatSidenavModule,
  MatListModule,
  MatGridListModule,
  MatDialogModule,
  MatSelectModule,
  MatDatepickerModule
];

@NgModule({
  imports: MATERIAL_IMPORTS,
  exports: MATERIAL_IMPORTS
})
export class MaterialModule { }
