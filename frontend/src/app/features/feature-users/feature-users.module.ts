import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { FeatureUsersRoutingModule } from './feature-users-routing.module';
import { PageUsersComponent } from './pages/page-users/page-users.component';
import { UsersTableComponent } from './components/users-table/users-table.component';
import { SharedModule } from 'src/app/shared/shared.module';
import { UserModalComponent } from './components/user-modal/user-modal.component';
import { UsersFilterComponent } from './components/users-filter/users-filter.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { UserUpdateModalComponent } from './components/user-update-modal/user-update-modal.component';
import { PageUserProfileComponent } from './pages/page-user-profile/page-user-profile.component';
import { UserProfileModalComponent } from './components/user-profile-modal/user-profile-modal.component';


@NgModule({
  declarations: [
    PageUsersComponent,
    UsersTableComponent,
    UserModalComponent,
    UsersFilterComponent,
    UserUpdateModalComponent,
    PageUserProfileComponent,
    UserProfileModalComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    FormsModule,
    ReactiveFormsModule,
    FeatureUsersRoutingModule
  ]
})
export class FeatureUsersModule { }
