import { TestBed } from '@angular/core/testing';

import { AdminReportesService } from './admin-reportes.service';

describe('AdminReportesService', () => {
  let service: AdminReportesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AdminReportesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
