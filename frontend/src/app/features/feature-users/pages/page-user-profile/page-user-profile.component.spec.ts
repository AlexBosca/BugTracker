import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PageUserProfileComponent } from './page-user-profile.component';

describe('PageUserProfileComponent', () => {
  let component: PageUserProfileComponent;
  let fixture: ComponentFixture<PageUserProfileComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PageUserProfileComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PageUserProfileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
