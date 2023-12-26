import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IssuesStatusPiechartComponent } from './issues-status-piechart.component';

describe('IssuesStatusPiechartComponent', () => {
  let component: IssuesStatusPiechartComponent;
  let fixture: ComponentFixture<IssuesStatusPiechartComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ IssuesStatusPiechartComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(IssuesStatusPiechartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
