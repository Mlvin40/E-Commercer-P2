// src/app/components/admin/historial-sanciones/historial-sanciones.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {
  AdminReportesService,
  Page,
  SancionHistRow
} from '../../../services/admin-reportes/admin-reportes.service';
import { finalize } from 'rxjs/operators';

@Component({
  standalone: true,
  selector: 'app-historial-sanciones',
  imports: [CommonModule, FormsModule],
  templateUrl: './historial-sanciones.component.html',
  styleUrls: ['./historial-sanciones.component.scss']
})
export class HistorialSancionesComponent implements OnInit {
  // filtros
  estado: string = ''; // '', 'ACTIVA', 'LEVANTADA'
  q: string = '';
  desde: string = '';
  hasta: string = '';

  // data
  page: Page<SancionHistRow> | null = null;
  loading = false;
  error: string | null = null;

  constructor(private api: AdminReportesService) {}

  ngOnInit(): void { this.load(); }

  load(p = 0) {
    this.loading = true; this.error = null;
    this.api.historialSanciones({
      estado: this.estado || undefined,
      q: this.q || undefined,
      desde: this.desde || undefined,
      hasta: this.hasta || undefined,
      page: p,
      size: 20
    }).pipe(finalize(()=> this.loading = false))
      .subscribe({
        next: res => this.page = res,
        error: e => this.error = e?.error?.message ?? e.message
      });
  }

  clearDates() { this.desde = ''; this.hasta = ''; }

  go(p: number) {
    if (!this.page) return;
    if (p < 0 || p >= this.page.totalPages) return;
    this.load(p);
  }
}
