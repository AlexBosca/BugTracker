import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProjectsIssuesBoardComponent } from './projects-issues-board.component';

describe('ProjectsIssuesBoardComponent', () => {
  let component: ProjectsIssuesBoardComponent;
  let fixture: ComponentFixture<ProjectsIssuesBoardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ProjectsIssuesBoardComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProjectsIssuesBoardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
