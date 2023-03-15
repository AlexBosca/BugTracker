import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { ToolbarComponent } from './core/components/toolbar/toolbar.component';
import { NavbarComponent } from './core/components/navbar/navbar.component';
import { FeatureIssuesModule } from './features/feature-issues/feature-issues.module';
import { FeatureTeamsModule } from './features/feature-teams/feature-teams.module';
import { FeatureProjectsModule } from './features/feature-projects/feature-projects.module';
import { FeatureAuthModule } from './features/feature-auth/feature-auth.module';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { BasicAuthInterceptor } from './core/interceptor/basic-auth.interceptor';

@NgModule({
  declarations: [
    AppComponent,
    ToolbarComponent,
    NavbarComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FeatureIssuesModule,
    FeatureTeamsModule,
    FeatureProjectsModule,
    FeatureAuthModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS, useClass: BasicAuthInterceptor, multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
