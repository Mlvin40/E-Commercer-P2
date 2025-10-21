import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {
  AdminReportesService,
  TopClientePedidosRow
} from '../../../services/admin-reportes/admin-reportes.service';
import { finalize } from 'rxjs/operators';

@Component({
  standalone: true,
  selector: 'app-top-clientes-pedidos',
  imports: [CommonModule, FormsModule],
  templateUrl: './top-clientes-pedidos.component.html',
  styleUrls: ['./top-clientes-pedidos.component.scss']
})
export class TopClientesPedidosComponent {
  desde = new Date(new Date().getFullYear(), new Date().getMonth(), 1).toISOString().slice(0,10);
  hasta = new Date().toISOString().slice(0,10);
  top = 10;

  loading = false;
  error: string | null = null;
  rows: TopClientePedidosRow[] = [];

  constructor(private api: AdminReportesService) {}

  buscar() {
    this.loading = true; this.error = null;
    this.api.topClientesPedidos(this.desde, this.hasta, this.top)
      .pipe(finalize(()=> this.loading = false))
      .subscribe({
        next: r => this.rows = r,
        error: e => this.error = e?.error?.message ?? e.message
      });
  }
}
