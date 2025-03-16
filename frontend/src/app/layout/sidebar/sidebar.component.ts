import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { MaterialModule } from '../../shared/material.import';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [RouterModule, MaterialModule],
  host: {
    '[style.--mat-sidenav-container-width]': '"240px"'
  },
  template: `
    <mat-sidenav-container class="container">
      <mat-sidenav mode="side" opened class="sidenav">
        <mat-nav-list>
          <a mat-list-item routerLink="/dashboard" class="nav-item">
            <mat-icon class="menu-icon">dashboard</mat-icon>
            <span class="menu-text">Dashboard</span>
          </a>
          <a mat-list-item routerLink="/projects" class="nav-item">
            <mat-icon class="menu-icon">folder</mat-icon>
            <span class="menu-text">Projects</span>
          </a>
          <a mat-list-item routerLink="/issues" class="nav-item">
            <mat-icon class="menu-icon">bug_report</mat-icon>
            <span class="menu-text">Issues</span>
          </a>

          <div class="spacer"></div>

          <a mat-list-item routerLink="/logout" class="nav-item">
            <mat-icon class="menu-icon">logout</mat-icon>
            <span class="menu-text">Logout</span>
          </a>
        </mat-nav-list>
      </mat-sidenav>
    </mat-sidenav-container>
  `,
  styles: [`
    * {
      padding: 0;
      // border-radius: 12px;
      // box-sizing: border-box;
    }

    .container {
      width: var(--sidenav-width);
      height: calc(100vh - 85px);
    }

    .sidenav {
      // width: 250px;
      flex-direction: column;
    }

    mat-nav-list {
      display: flex;
      flex-direction: column;
      height: 100%;
    }

    .nav-item {
      display: flex;
      align-items: center; /* Vertical alignment */
      gap: 0.75rem; /* Spacing between icon and text */
      padding: 1rem;
      text-decoration: none; /* Remove default link styling */
    }

    .menu-icon {
      vertical-align: middle;
      // padding: 0.5rem;
    }

    .menu-text {
      vertical-align: middle;
      padding: 0.5rem;
    }

    .spacer {
      flex-grow: 1; /* Pushes the logout button to the bottom */
    }
  `]
})
export class SidebarComponent {
  menus = [
    {
      title: 'Projects',
      icon: 'folder',
      items: [
        { label: 'Project A', link: '/projects/a' },
        { label: 'Project B', link: '/projects/b' }
      ]
    },
    {
      title: 'Issues',
      icon: 'bug_report',
      items: [
        { label: 'Open Issues', link: '/issues/open' },
        { label: 'Closed Issues', link: '/issues/closed' }
      ]
    },
    {
      title: 'Admin',
      icon: 'settings',
      items: [
        { label: 'User Management', link: '/admin/users' },
        { label: 'System Settings', link: '/admin/settings' }
      ]
    }
  ];

  onLogout() {
    // Handle logout logic here
  }
}
