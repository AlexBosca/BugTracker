import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { FeatureTeamsRoutingModule } from './feature-teams-routing.module';
import { TeamCardComponent } from './components/team-card/team-card.component';
import { TeamsGridComponent } from './components/teams-grid/teams-grid.component';
import { PageTeamsComponent } from './pages/page-teams/page-teams.component';


@NgModule({
  declarations: [
    TeamCardComponent,
    TeamsGridComponent,
    PageTeamsComponent
  ],
  imports: [
    CommonModule,
    FeatureTeamsRoutingModule
  ]
})
export class FeatureTeamsModule { }
