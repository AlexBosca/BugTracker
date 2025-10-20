import { ComponentFixture, TestBed } from '@angular/core/testing';
import { SidebarComponent } from './sidebar.component';
import { AuthService } from '../../services/auth.service';
import { provideRouter } from '@angular/router';

describe('SidebarComponent', () => {
  let component: SidebarComponent;
  let fixture: ComponentFixture<SidebarComponent>;
  let authServiceSpy: jasmine.SpyObj<AuthService>;

  beforeEach(async () => {
    authServiceSpy = jasmine.createSpyObj('AuthService', ['logout']);

    await TestBed.configureTestingModule({
      imports: [SidebarComponent],
      providers: [
        { provide: AuthService, useValue: authServiceSpy },
        provideRouter([]) // needed because RouterModule is imported in SidebarComponent
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(SidebarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  // ----------------------
  // 1️⃣ Creation
  // ----------------------
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // ----------------------
  // 2️⃣ onLogout()
  // ----------------------
  it('should call authService.logout() when onLogout is called', () => {
    const event = new MouseEvent('click');
    spyOn(event, 'preventDefault');

    component.onLogout(event);

    expect(event.preventDefault).toHaveBeenCalled();
    expect(authServiceSpy.logout).toHaveBeenCalled();
  });

  // ----------------------
  // 3️⃣ onKeyDown()
  // ----------------------
  it('should call onLogout when Enter is pressed', () => {
    const event = new KeyboardEvent('keydown', { key: 'Enter' });
    spyOn(event, 'preventDefault');
    spyOn(component, 'onLogout');

    component.onKeyDown(event);

    expect(event.preventDefault).toHaveBeenCalled();
    expect(component.onLogout).toHaveBeenCalledWith(event);
  });

  it('should call onLogout when Space is pressed', () => {
    const event = new KeyboardEvent('keydown', { key: ' ' });
    spyOn(event, 'preventDefault');
    spyOn(component, 'onLogout');

    component.onKeyDown(event);

    expect(event.preventDefault).toHaveBeenCalled();
    expect(component.onLogout).toHaveBeenCalledWith(event);
  });

  it('should not call onLogout for other keys', () => {
    const event = new KeyboardEvent('keydown', { key: 'Escape' });
    spyOn(component, 'onLogout');

    component.onKeyDown(event);

    expect(component.onLogout).not.toHaveBeenCalled();
  });

  // ----------------------
  // 4️⃣ DOM interaction
  // ----------------------
  it('should call onLogout when logout link is clicked', () => {
    spyOn(component, 'onLogout');
    fixture.detectChanges();

    const logoutLink = fixture.nativeElement.querySelector('.nav-item:last-child');
    logoutLink.dispatchEvent(new MouseEvent('click'));

    expect(component.onLogout).toHaveBeenCalled();
  });

  it('should call onKeyDown when key is pressed on logout link', () => {
    spyOn(component, 'onKeyDown');
    fixture.detectChanges();

    const logoutLink = fixture.nativeElement.querySelector('.nav-item:last-child');
    const event = new KeyboardEvent('keydown', { key: 'Enter' });
    logoutLink.dispatchEvent(event);

    expect(component.onKeyDown).toHaveBeenCalled();
  });
});
