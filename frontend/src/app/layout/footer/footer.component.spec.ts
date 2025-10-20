import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FooterComponent } from './footer.component';

describe('FooterComponent', () => {
  let component: FooterComponent;
  let fixture: ComponentFixture<FooterComponent>;
  let compiled: HTMLElement;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FooterComponent]
    }).compileComponents();

    fixture = TestBed.createComponent(FooterComponent);
    component = fixture.componentInstance;
    compiled = fixture.nativeElement;
    fixture.detectChanges();
  });

  // 1️⃣ Component creation
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // 2️⃣ currentYear should match actual year
  it('should set currentYear to the current year', () => {
    const currentYear = new Date().getFullYear();
    expect(component.currentYear).toBe(currentYear);
  });

  // 3️⃣ DOM should render the footer text correctly
  it('should render the current year and copyright text', () => {
    const footerText = compiled.querySelector('p')?.textContent;
    const currentYear = new Date().getFullYear().toString();

    expect(footerText).toContain(currentYear);
    expect(footerText).toContain('Bug Tracker');
    expect(footerText).toContain('All rights reserved.');
  });

  // 4️⃣ Footer element should exist
  it('should have a footer element with class "footer"', () => {
    const footerElement = compiled.querySelector('footer');
    expect(footerElement).toBeTruthy();
    expect(footerElement?.classList).toContain('footer');
  });
});
