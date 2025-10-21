import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClientesProductosVentaComponent } from './clientes-productos-venta.component';

describe('ClientesProductosVentaComponent', () => {
  let component: ClientesProductosVentaComponent;
  let fixture: ComponentFixture<ClientesProductosVentaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ClientesProductosVentaComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ClientesProductosVentaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
