import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IssuesStatusByTeamComponent } from './issues-status-by-team.component';

describe('IssuesStatusByTeamComponent', () => {
  let component: IssuesStatusByTeamComponent;
  let fixture: ComponentFixture<IssuesStatusByTeamComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ IssuesStatusByTeamComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(IssuesStatusByTeamComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
