import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { NotificationService } from '../../services/notification.service';
import { Notification } from '../../models/notification.model';
import { MaterialModule } from '../../shared/material.import';

@Component({
  selector: 'app-top-nav',
  standalone: true,
  imports: [CommonModule, FormsModule, MaterialModule],
  host: {
    '[style.--mat-form-field-container-vertical-padding]': '"0px"',
    '[style.--mat-form-field-container-height]': '"40px"'
  },
  template: `
    <mat-toolbar class="mat-toolbar">
      <div class="logo">
        <mat-icon>bug_report</mat-icon>
        <span>Bug Tracker</span>
      </div>

      <div class="search-bar">
        <mat-form-field appearance="outline">
          <mat-label>Search issues...</mat-label>
          <input matInput [(ngModel)]="searchQuery" (keyup)="onSearch()" placeholder="Search...">
          <mat-icon matSuffix>search</mat-icon>
        </mat-form-field>
      </div>

      <div class="actions">
        <button mat-icon-button [matMenuTriggerFor]="notificationsMenu" aria-label="Notifications">
          <mat-icon matBadge={{getUnreadNotifications()}} matBadgeSize="large" [matBadgeHidden]=allNotificationsRead() aria-hidden="false">notifications</mat-icon>
        </button>
        <mat-menu #notificationsMenu="matMenu" [hasBackdrop]="true" xPosition="before" overlapTrigger="false" class="notification-menu">
          <div class="notification-menu-header">
            <h3>Notifications</h3>
            <div>
              <button mat-button (click)="readAllNotifications($event)">
                <mat-icon>visibility</mat-icon>
                Read all
              </button>
              <button mat-button (click)="cleanAllNotifications($event)">
                <mat-icon>clear_all</mat-icon>
                Clear all
              </button>
            </div>
          </div>
          @if (notifications.length === 0) {
            <p>No notifications</p>
          } @else {
            <mat-divider></mat-divider>
            @for (notification of notifications; track notification.id) {
              <button class="notification" [ngClass]="{'unread': notification.unread}" mat-menu-item (click)="handleNotificationClick(notification, $event)">
                <mat-icon>notifications</mat-icon>
                <p class="notification-message">{{notification.message}}</p>
                <span class="notification-creation-date">{{notification.createdAt | date:"MMM dd, yyyy 'at' hh:mm a"}}</span>
              </button>
            }
          }
        </mat-menu>

        <button mat-icon-button [matMenuTriggerFor]="settingsMenu" aria-label="Settings">
          <mat-icon>settings</mat-icon>
        </button>
        <mat-menu #settingsMenu="matMenu" xPosition="before" class="settings-menu">
          <h3>Settings</h3>
          <button mat-menu-item>
            <mat-icon>desktop_windows</mat-icon>
            <p>Systems</p>
          </button>
          <button mat-menu-item>
            <mat-icon>web</mat-icon>
            <p>Products</p>
          </button>
          <button mat-menu-item>
            <mat-icon>apps</mat-icon>
            <p>Apps</p>
          </button>
        </mat-menu>

        <button mat-icon-button [matMenuTriggerFor]="profileMenu" aria-label="User Profile">
          <mat-icon>account_circle</mat-icon>
        </button>
        <mat-menu #profileMenu="matMenu" xPosition="before" class="account-menu">
          <h3>Account</h3>
          <button mat-menu-item>
            <mat-icon>manage_accounts</mat-icon>
            <p>Manage account</p>
          </button>
          <button mat-menu-item>
            <mat-icon>portrait</mat-icon>
            <p>Profile</p>
          </button>
          <button mat-menu-item>
            <mat-icon>settings_account_box</mat-icon>
            <p>Personal settings</p>
          </button>
          <button mat-menu-item>
            <mat-icon>palette</mat-icon>
            <p>Theme</p>
          </button>
        </mat-menu>
      </div>
    </mat-toolbar>
  `,
  styles: [`
    ::ng-deep .notification-menu.mat-mdc-menu-panel {
      min-width: 350px !important; /* Adjust to your desired width */
      max-width: none !important;   /* Remove default max-width */
      width: 600px;
      padding: 0 1rem;
    }

    ::ng-deep .settings-menu.mat-mdc-menu-panel {
      max-width: none !important;   /* Remove default max-width */
      width: 250px;
      padding: 0 1rem;
    }

    ::ng-deep .account-menu.mat-mdc-menu-panel {
      max-width: none !important;   /* Remove default max-width */
      width: 250px;
      padding: 0 1rem;
    }

    mat-toolbar {
      display: flex;
      align-items: center; /* ✅ Ensures vertical alignment */
      justify-content: space-between; /* ✅ Aligns logo, search bar, and actions */
      padding: 0 1rem;
      height: 64px; /* ✅ Matches Material toolbar height */
    }

    p {
      margin: 0;
    }

    .notification {
      padding: 1rem;
      margin: 0.5rem 0;
      border-radius: 4px;
      display: flex;
      align-items: center;
      gap: 1rem;
    }

    .notification-menu-header {
      display: flex;
      align-items: center;
      justify-content: space-between;
    }

    .notification-message {

    }

    .notification-creation-date {
      opacity: 0.6;
    }

    mat-divider {
      margin-bottom: 1rem;
      opacity: 0.6;
    }

    .unread {
      background-color:rgb(75, 75, 75);
      font-weight: bold;
    }

    .logo {
      width: calc(var(--sidenav-width));
      display: flex;
      align-items: center;
      gap: 0.5rem;
    }

    .search-bar {
      flex: 1;
      display: flex; /* ✅ Ensures the search bar aligns properly */
    }

    mat-form-field {
      max-width: 1300px;
      width: 100%;
      margin: 0;
      height: 40px; /* ✅ Matches button and toolbar height */
    }

    .actions {
      display: flex;
      gap: 1rem;
      align-items: center; /* ✅ Ensures icons are aligned properly */
    }

    input {
      padding: 0.5rem; /* ✅ Adjusts spacing inside input */
    }
  `]
})
export class TopNavComponent implements OnInit {
  searchQuery = '';
  notifications: Notification[] = [];

  constructor(
    readonly notificationService: NotificationService
  ) { }

  ngOnInit(): void {
    this.fetchNotifications();
  }

  onSearch() {
    console.log('Searching for:', this.searchQuery);
    // Add your search logic here (API calls, filtering, etc.)
  }

  fetchNotifications(): void {
    this.notificationService.fetchNotifications()
      .subscribe(notifications => this.notifications = notifications);
  }

  getUnreadNotifications() : string {
    let unreadNotifications = 0;

    for (const notification of this.notifications) {
      if (notification.unread) {
        unreadNotifications++;
      }
    }

    return unreadNotifications <= 4 ? unreadNotifications.toString() : '4+';
  }

  allNotificationsRead() : boolean {
    for (const notification of this.notifications) {
      if (notification.unread) {
        return false;
      }
    }

    return true;
  }

  handleNotificationClick(notification: Notification, event: Event) {
    event.stopPropagation();
    this.readNotification(notification);
  }

  readNotification(notification: Notification) {
    notification.unread = false;
  }

  readAllNotifications(event: Event) {
    event.stopPropagation();

    for (const notification of this.notifications) {
      notification.unread = false;
    }
  }

  cleanAllNotifications(event: Event) {
    event.stopPropagation();
    this.notifications = [];
  }
}

