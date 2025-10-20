import { TestBed } from '@angular/core/testing';

import { RatingsPublicService } from './ratings-public.service';

describe('RatingsPublicService', () => {
  let service: RatingsPublicService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RatingsPublicService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
