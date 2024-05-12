import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ToolbarComponent } from './core/components/toolbar/toolbar.component';
import { NavbarComponent } from './core/components/navbar/navbar.component';
import { FeatureIssuesModule } from './features/feature-issues/feature-issues.module';
import { FeatureProjectsModule } from './features/feature-projects/feature-projects.module';
import { FeatureAuthModule } from './features/feature-auth/feature-auth.module';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { BasicAuthInterceptor } from './core/interceptor/basic-auth.interceptor';
import { FeatureUsersModule } from './features/feature-users/feature-users.module';
import { AccessDeniedComponent } from './core/components/access-denied/access-denied.component';

@NgModule({
  declarations: [
    AppComponent,
    ToolbarComponent,
    NavbarComponent,
    AccessDeniedComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FeatureIssuesModule,
    FeatureProjectsModule,
    FeatureUsersModule,
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
