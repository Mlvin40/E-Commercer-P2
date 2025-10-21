import { TestBed } from '@angular/core/testing';

import { LogisticaLogistica } from './logistica.logistica';

describe('LogisticaLogistica', () => {
  let service: LogisticaLogistica;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LogisticaLogistica);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
