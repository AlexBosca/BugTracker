import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProjectPeopleModalComponent } from './project-people-modal.component';

describe('ProjectPeopleModalComponent', () => {
  let component: ProjectPeopleModalComponent;
  let fixture: ComponentFixture<ProjectPeopleModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ProjectPeopleModalComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProjectPeopleModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
