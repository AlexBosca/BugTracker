import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PageIssuesComponent } from './page-issues.component';

describe('PageIssuesComponent', () => {
  let component: PageIssuesComponent;
  let fixture: ComponentFixture<PageIssuesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PageIssuesComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PageIssuesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
