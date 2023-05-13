import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PageProjectsDetailsComponent } from './page-projects-details.component';

describe('PageProjectsDetailsComponent', () => {
  let component: PageProjectsDetailsComponent;
  let fixture: ComponentFixture<PageProjectsDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PageProjectsDetailsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PageProjectsDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
