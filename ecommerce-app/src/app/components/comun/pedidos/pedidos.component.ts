import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PedidosService, PedidoListItem } from '../../../services/pedidos/pedidos.service';

@Component({
  standalone: true,
  selector: 'app-pedidos',
  imports: [CommonModule],
  templateUrl: './pedidos.component.html'
})
export class PedidosComponent implements OnInit {
  items: PedidoListItem[] = [];
  cargando=false; error:string|null=null;

  constructor(private api: PedidosService) {}

  ngOnInit() { this.load(); }
  load() {
    this.cargando = true; this.error = null;
    this.api.listar().subscribe({
      next: r => { this.items = r; this.cargando = false; },
      error: e => { this.error = e?.error?.message || 'Error'; this.cargando = false; }
    });
  }
}
