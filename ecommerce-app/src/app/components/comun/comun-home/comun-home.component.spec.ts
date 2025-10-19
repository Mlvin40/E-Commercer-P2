import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ComunHomeComponent } from './comun-home.component';

describe('ComunHomeComponent', () => {
  let component: ComunHomeComponent;
  let fixture: ComponentFixture<ComunHomeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ComunHomeComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ComunHomeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
