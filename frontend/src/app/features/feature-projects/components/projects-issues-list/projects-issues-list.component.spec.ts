import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProjectsIssuesListComponent } from './projects-issues-list.component';

describe('ProjectsIssuesListComponent', () => {
  let component: ProjectsIssuesListComponent;
  let fixture: ComponentFixture<ProjectsIssuesListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ProjectsIssuesListComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProjectsIssuesListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
