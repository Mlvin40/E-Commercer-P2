import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CarritoService, CarritoView, CarritoItem } from '../../../services/carrito/carrito.service';
import { CheckoutService, CheckoutRequest, Tarjeta } from '../../../services/checkout/checkout.service';

@Component({
  standalone: true,
  selector: 'app-carrito',
  imports: [CommonModule, FormsModule],
  templateUrl: './carrito.component.html'
})
export class CarritoComponent implements OnInit {
  data: CarritoView | null = null;
  cargando = false;
  error: string | null = null;

  tarjetas: Tarjeta[] = [];
  usarGuardada = true;
  savedCardId?: number;

  numero = '';
  expMes = '';
  expAnio = '';
  titular = '';
  guardar = true;

  constructor(private cart: CarritoService, private checkout: CheckoutService) {}

  ngOnInit() {
    this.load();
    this.checkout.tarjetas().subscribe(t => this.tarjetas = t);
  }

  // === GETTERS SEGUROS PARA EL TEMPLATE ===
  get items(): CarritoItem[] {
    return this.data?.items ?? [];
  }
  get hasItems(): boolean {
    return this.items.length > 0;
  }
  get subtotal(): number {
    return this.data?.subtotal ?? 0;
  }

  load() {
    this.cargando = true; this.error = null;
    this.cart.ver().subscribe({
      next: res => { this.data = res; this.cargando = false; },
      error: e => { this.error = e?.error?.message || 'Error'; this.cargando = false; }
    });
  }

  cambiarCantidad(pId: number, valor: any) {
    const cantidad = Math.max(1, Number(valor ?? 1));
    this.cart.update(pId, cantidad).subscribe({
      next: res => this.data = res,
      error: e => this.error = e?.error?.message || 'Error'
    });
  }

  quitar(pId: number) {
    this.cart.remove(pId).subscribe({
      next: res => this.data = res,
      error: e => this.error = e?.error?.message || 'Error'
    });
  }

  vaciar() {
    this.cart.clear().subscribe({
      next: () => this.load(),
      error: e => this.error = e?.error?.message || 'Error'
    });
  }

  pagar() {
    const body: CheckoutRequest = this.usarGuardada
      ? { savedCardId: this.savedCardId }
      : { numero: this.numero, expMes: this.expMes, expAnio: this.expAnio, titular: this.titular, guardar: this.guardar };

    this.checkout.pagar(body).subscribe({
      next: r => { alert(`Pago ${r.estadoPago}. Pedido #${r.pedidoId}`); this.load(); },
      error: e => this.error = e?.error?.message || 'No se pudo pagar'
    });
  }
}
