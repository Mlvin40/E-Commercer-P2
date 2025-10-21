import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TopClientesPedidosComponent } from './top-clientes-pedidos.component';

describe('TopClientesPedidosComponent', () => {
  let component: TopClientesPedidosComponent;
  let fixture: ComponentFixture<TopClientesPedidosComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TopClientesPedidosComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TopClientesPedidosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
