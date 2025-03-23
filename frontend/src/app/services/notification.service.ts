import { Injectable } from '@angular/core';
import { Notification } from '../models/notification.model';
import { Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  constructor() { }

  public fetchNotifications(): Observable<Notification[]> {
    return of([
      {
        id: '1',
        message: 'New issue reported',
        createdAt: new Date("07-Jul-2024"),
        unread: false
      },
      {
        id: '2',
        message: 'New issue reported',
        createdAt: new Date("14-Aug-2024"),
        unread: false
      },
      {
        id: '3',
        message: 'New issue reported',
        createdAt: new Date("04-Sep-2024"),
        unread: true
      },
      {
        id: '4',
        message: 'New issue reported',
        createdAt: new Date("18-Sep-2024"),
        unread: true
      },
      {
        id: '5',
        message: 'New issue reported',
        createdAt: new Date("25-Jan-2025"),
        unread: true
      }
    ]);
  }
}
