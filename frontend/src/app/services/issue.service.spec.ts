import { TestBed } from '@angular/core/testing';

import { IssueService } from './issue.service';
import { provideHttpClient } from '@angular/common/http';

describe('IssueService', () => {
  let service: IssueService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient()]
    });
    service = TestBed.inject(IssueService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
