// src/app/components/admin/top-clientes-ganancia/top-clientes-ganancia.component.ts
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AdminReportesService, TopVendedorGananciaRow } from '../../../services/admin-reportes/admin-reportes.service';
import { finalize } from 'rxjs/operators';

@Component({
  standalone: true,
  selector: 'app-top-clientes-ganancia',
  imports: [CommonModule, FormsModule],
  templateUrl: './top-clientes-ganancia.component.html'
})
export class TopClientesGananciaComponent {
  desde = new Date(new Date().getFullYear(), new Date().getMonth(), 1).toISOString().slice(0,10);
  hasta = new Date().toISOString().slice(0,10);
  top = 5;

  loading = false;
  error: string | null = null;

  rows: TopVendedorGananciaRow[] = [];

  constructor(private api: AdminReportesService) {}

  buscar() {
    this.loading = true; this.error = null;
    this.api.topVendedoresGanancia(this.desde, this.hasta, this.top)
      .pipe(finalize(()=> this.loading = false))
      .subscribe({
        next: r => this.rows = r,
        error: e => this.error = e?.error?.message ?? e.message
      });
  }
}
