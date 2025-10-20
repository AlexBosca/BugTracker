import { TestBed } from '@angular/core/testing';

import { TopNavComponent } from './top-nav.component';

describe('TopNavComponent', () => {
  let component: TopNavComponent;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TopNavComponent]
    });
    const fixture = TestBed.createComponent(TopNavComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have default searchQuery as empty string', () => {
    expect(component.searchQuery).toBe('');
  });

  it('should have notifications array initialized', () => {
    expect(component.notifications).toBeDefined();
    expect(Array.isArray(component.notifications)).toBeTrue();
  });

  it('should call onSearch and log the search query', () => {
    spyOn(console, 'log');
    component.searchQuery = 'test';
    component.onSearch();
    expect(console.log).toHaveBeenCalledWith('Searching for:', 'test');
  });

  it('should fetch notifications on ngOnInit', () => {
    spyOn(component, 'fetchNotifications');
    component.ngOnInit();
    expect(component.fetchNotifications).toHaveBeenCalled();
  });

  it('should read a notification', () => {
    const notification = { id: '1', message: 'Test', createdAt: new Date(), unread: true };
    component.readNotification(notification);
    expect(notification.unread).toBeFalse();
  });

  it('should mark all notifications as read', () => {
    component.notifications = [
      { id: '1', message: 'Test 1', createdAt: new Date(), unread: true },
      { id: '2', message: 'Test 2', createdAt: new Date(), unread: true }
    ];

    component.readAllNotifications(new Event('click'));

    for (const notification of component.notifications) {
      expect(notification.unread).toBeFalse();
    }
  });

  it('should clean all notifications', () => {
    component.notifications = [
      { id: '1', message: 'Test 1', createdAt: new Date(), unread: true },
      { id: '2', message: 'Test 2', createdAt: new Date(), unread: false }
    ];

    component.cleanAllNotifications(new Event('click'));

    expect(component.notifications.length).toBe(0);
  });

  it('should handle notification click and read the notification', () => {
    const notification = { id: '1', message: 'Test', createdAt: new Date(), unread: true };
    const event = new Event('click');
    spyOn(event, 'stopPropagation');
    component.handleNotificationClick(notification, event);
    expect(event.stopPropagation).toHaveBeenCalled();
    expect(notification.unread).toBeFalse();
  });

  it('should return correct unread notifications count', () => {
    component.notifications = [
      { id: '1', message: 'Test 1', createdAt: new Date(), unread: true },
      { id: '2', message: 'Test 2', createdAt: new Date(), unread: false },
      { id: '3', message: 'Test 3', createdAt: new Date(), unread: true },
      { id: '4', message: 'Test 4', createdAt: new Date(), unread: true },
      { id: '5', message: 'Test 5', createdAt: new Date(), unread: true },
      { id: '6', message: 'Test 6', createdAt: new Date(), unread: true }
    ];

    expect(component.getUnreadNotifications()).toBe('4+');

    component.notifications[0].unread = false;
    component.notifications[2].unread = false;

    expect(component.getUnreadNotifications()).toBe('3');
  });

  it('should return true if all notifications are read', () => {
    component.notifications = [
      { id: '1', message: 'Test 1', createdAt: new Date(), unread: false },
      { id: '2', message: 'Test 2', createdAt: new Date(), unread: false }
    ];

    expect(component.allNotificationsRead()).toBeTrue();
  });

  it('should return false if there are unread notifications', () => {
    component.notifications = [
      { id: '1', message: 'Test 1', createdAt: new Date(), unread: false },
      { id: '2', message: 'Test 2', createdAt: new Date(), unread: true }
    ];

    expect(component.allNotificationsRead()).toBeFalse();
  });

  it('should stop event propagation in readAllNotifications', () => {
    const event = new Event('click');
    spyOn(event, 'stopPropagation');
    component.readAllNotifications(event);
    expect(event.stopPropagation).toHaveBeenCalled();
  });

  it('should stop event propagation in cleanAllNotifications', () => {
    const event = new Event('click');
    spyOn(event, 'stopPropagation');
    component.cleanAllNotifications(event);
    expect(event.stopPropagation).toHaveBeenCalled();
  });

  it('should stop event propagation in handleNotificationClick', () => {
    const notification = { id: '1', message: 'Test', createdAt: new Date(), unread: true };
    const event = new Event('click');
    spyOn(event, 'stopPropagation');
    component.handleNotificationClick(notification, event);
    expect(event.stopPropagation).toHaveBeenCalled();
  });
});
