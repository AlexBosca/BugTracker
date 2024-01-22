import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IssuesStatusByAssigneeComponent } from './issues-status-by-assignee.component';

describe('IssuesStatusByAssigneeComponent', () => {
  let component: IssuesStatusByAssigneeComponent;
  let fixture: ComponentFixture<IssuesStatusByAssigneeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ IssuesStatusByAssigneeComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(IssuesStatusByAssigneeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
