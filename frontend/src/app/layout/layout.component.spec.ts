import { ComponentFixture, TestBed } from '@angular/core/testing';
import { LayoutComponent } from './layout.component';
import { provideRouter, RouterOutlet } from '@angular/router';
import { SidebarComponent } from './sidebar/sidebar.component';
import { TopNavComponent } from './top-nav/top-nav.component';
import { FooterComponent } from './footer/footer.component';
import { AuthService } from '../services/auth.service';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';

describe('LayoutComponent', () => {
  let fixture: ComponentFixture<LayoutComponent>;
  let component: LayoutComponent;
  let compiled: HTMLElement;

  beforeEach(async () => {
    const authServiceSpy = jasmine.createSpyObj('AuthService', ['logout']);

    await TestBed.configureTestingModule({
      imports: [
        LayoutComponent,
        SidebarComponent,
        TopNavComponent,
        FooterComponent,
        RouterOutlet,
        NoopAnimationsModule
      ],
      providers: [
        { provide: AuthService, useValue: authServiceSpy },
        provideRouter([])
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(LayoutComponent);
    component = fixture.componentInstance;
    compiled = fixture.nativeElement;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should render app-top-nav, app-sidebar, router-outlet, and app-footer', () => {
    expect(compiled.querySelector('app-top-nav')).toBeTruthy();
    expect(compiled.querySelector('app-sidebar')).toBeTruthy();
    expect(compiled.querySelector('router-outlet')).toBeTruthy();
    expect(compiled.querySelector('app-footer')).toBeTruthy();
  });

  it('should have elements with classes "layout" and "content"', () => {
    const layoutDiv = compiled.querySelector('.layout');
    const contentDiv = compiled.querySelector('.content');

    expect(layoutDiv).toBeTruthy();
    expect(contentDiv).toBeTruthy();
    expect(layoutDiv?.contains(contentDiv!)).toBeTrue();
  });

  it('should render app-footer inside main element', () => {
    const main = compiled.querySelector('main');
    const footer = main?.querySelector('app-footer');
    expect(footer).toBeTruthy();
  });

  it('should render sidebar before main in the content section', () => {
    const content = compiled.querySelector('.content');
    const children = Array.from(content?.children || []);
    expect(children[0].tagName.toLowerCase()).toBe('app-sidebar');
    expect(children[1].tagName.toLowerCase()).toBe('main');
  });
});
