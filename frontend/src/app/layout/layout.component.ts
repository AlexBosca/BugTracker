import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { SidebarComponent } from './sidebar/sidebar.component';
import { TopNavComponent } from './top-nav/top-nav.component';
import { MaterialModule } from '../shared/material.import';
import { FooterComponent } from "./footer/footer.component";

@Component({
  selector: 'app-layout',
  imports: [CommonModule, RouterModule, MaterialModule, TopNavComponent, SidebarComponent, FooterComponent],

  template: `
    <div class="layout">
      <app-top-nav></app-top-nav>
      <div class="content">
        <app-sidebar></app-sidebar>
        <main>
          <router-outlet></router-outlet>
          <app-footer></app-footer>
        </main>
      </div>
    </div>
  `,
  styles: [`
    app-sidebar {
      padding: 5px;
      border-right: 1px solid var(--mat-sys-surface-variant);
    }
    app-top-nav {
      border-bottom: 1px solid var(--mat-sys-surface-variant);
    }
    app-footer {
      text-align: center;
      padding: 1rem;
      color: #333;
      bottom: 0;
    }
    mat-card-title, mat-card-content {
      color: var(--mat-sys-on-surface-variant);
    }
    .margins {
      margin: 0.5rem;
    }
    .layout {
      display: flex;
      flex-direction: column;
      height: 100vh;
    }
    .content {
      display: flex;
      flex: 1;
    }
    main {
      flex: 1;
    }
    .example-card {
    }
    .example-card-footer {
      padding: 16px;
    }
  `]
})
export class LayoutComponent { }
