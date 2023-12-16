import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PageAuthenticationComponent } from './page-authentication.component';

describe('AuthenticationComponent', () => {
  let component: PageAuthenticationComponent;
  let fixture: ComponentFixture<PageAuthenticationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PageAuthenticationComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PageAuthenticationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
