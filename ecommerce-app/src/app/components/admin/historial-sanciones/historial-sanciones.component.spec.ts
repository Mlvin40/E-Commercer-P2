import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HistorialSancionesComponent } from './historial-sanciones.component';

describe('HistorialSancionesComponent', () => {
  let component: HistorialSancionesComponent;
  let fixture: ComponentFixture<HistorialSancionesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HistorialSancionesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HistorialSancionesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
