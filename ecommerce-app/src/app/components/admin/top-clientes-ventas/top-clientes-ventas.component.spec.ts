import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TopClientesVentasComponent } from './top-clientes-ventas.component';

describe('TopClientesVentasComponent', () => {
  let component: TopClientesVentasComponent;
  let fixture: ComponentFixture<TopClientesVentasComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TopClientesVentasComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TopClientesVentasComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
