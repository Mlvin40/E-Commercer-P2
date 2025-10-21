// src/app/components/admin/clientes-productos-venta/clientes-productos-venta.component.ts
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {
  AdminReportesService,
  TopClienteProductosVentaRow
} from '../../../services/admin-reportes/admin-reportes.service';
import { finalize } from 'rxjs/operators';

@Component({
  standalone: true,
  selector: 'app-clientes-productos-venta',
  imports: [CommonModule, FormsModule],
  templateUrl: './clientes-productos-venta.component.html',
  styleUrls: ['./clientes-productos-venta.component.scss']
})
export class ClientesProductosVentaComponent {
  top = 10;

  loading = false;
  error: string | null = null;
  rows: TopClienteProductosVentaRow[] = [];

  constructor(private api: AdminReportesService) {}

  buscar() {
    this.loading = true; this.error = null;
    this.api.topClientesProductosVenta(this.top)
      .pipe(finalize(()=> this.loading = false))
      .subscribe({
        next: r => this.rows = r,
        error: e => this.error = e?.error?.message ?? e.message
      });
  }
}
