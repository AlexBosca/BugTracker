import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IssuesPriorityPiechartComponent } from './issues-priority-piechart.component';

describe('IssuesPriorityPiechartComponent', () => {
  let component: IssuesPriorityPiechartComponent;
  let fixture: ComponentFixture<IssuesPriorityPiechartComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ IssuesPriorityPiechartComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(IssuesPriorityPiechartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
