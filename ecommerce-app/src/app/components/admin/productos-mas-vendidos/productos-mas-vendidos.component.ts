// src/app/components/admin/productos-mas-vendidos/productos-mas-vendidos.component.ts
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AdminReportesService, TopProductoRow } from '../../../services/admin-reportes/admin-reportes.service';
import { finalize } from 'rxjs/operators';

@Component({
  standalone: true,
  selector: 'app-productos-mas-vendidos',
  imports: [CommonModule, FormsModule],
  templateUrl: './productos-mas-vendidos.component.html'
})
export class ProductosMasVendidosComponent {
  desde = new Date(new Date().getFullYear(), new Date().getMonth(), 1).toISOString().slice(0,10);
  hasta = new Date().toISOString().slice(0,10);
  limit = 10;

  loading = false;
  error: string | null = null;

  rows: TopProductoRow[] = [];

  constructor(private api: AdminReportesService) {}

  buscar() {
    this.loading = true; this.error = null;
    this.api.topProductos(this.desde, this.hasta, this.limit)
      .pipe(finalize(()=> this.loading = false))
      .subscribe({
        next: r => this.rows = r,
        error: e => this.error = e?.error?.message ?? e.message
      });
  }
}
