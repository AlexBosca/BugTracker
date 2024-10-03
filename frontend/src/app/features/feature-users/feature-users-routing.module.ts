import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PageUsersComponent } from './pages/page-users/page-users.component';
import { PageUserProfileComponent } from './pages/page-user-profile/page-user-profile.component';

const routes: Routes = [
  {
    path: '',
    component: PageUsersComponent
  },
  {
    path: 'profile',
    component: PageUserProfileComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class FeatureUsersRoutingModule { }
