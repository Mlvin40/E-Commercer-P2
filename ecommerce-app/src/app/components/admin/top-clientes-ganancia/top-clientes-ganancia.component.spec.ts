import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TopClientesGananciaComponent } from './top-clientes-ganancia.component';

describe('TopClientesGananciaComponent', () => {
  let component: TopClientesGananciaComponent;
  let fixture: ComponentFixture<TopClientesGananciaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TopClientesGananciaComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TopClientesGananciaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
