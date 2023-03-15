import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PageIssueDetailsComponent } from './page-issue-details.component';

describe('PageIssueDetailsComponent', () => {
  let component: PageIssueDetailsComponent;
  let fixture: ComponentFixture<PageIssueDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PageIssueDetailsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PageIssueDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
