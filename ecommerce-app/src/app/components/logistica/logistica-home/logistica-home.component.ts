
import { Component, OnInit } from '@angular/core';
import { LogisticaService, Page, PedidoRow } from '../../../services/logistica/logistica.logistica';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { finalize } from 'rxjs/operators';
import { LogoutButtonComponent } from '../../portal/logout-button/logout-button.component';

@Component({
  selector: 'app-logistica-home',
  standalone: true,
  imports: [CommonModule, FormsModule, LogoutButtonComponent],
  templateUrl: './logistica-home.component.html',
  styleUrls: ['./logistica-home.component.scss']
})
export class LogisticaHomeComponent implements OnInit {
  page: Page<PedidoRow> | null = null;
  loading = false;
  savingId: number | null = null;
  selected: Record<number, string> = {}; // id -> nueva fecha

  constructor(private api: LogisticaService) {}

  ngOnInit(): void { this.load(); }

  load(p=0) {
    this.loading = true;
    this.api.listar(p, 20).pipe(finalize(()=> this.loading=false)).subscribe({
      next: res => this.page = res,
      error: e => alert('No se pudo cargar: ' + (e?.error?.message ?? e.message))
    });
  }

  reprogramar(p: PedidoRow) {
    const fecha = this.selected[p.id] || p.fechaEstimadaEntrega;
    if (!fecha) return;
    this.savingId = p.id;
    this.api.reprogramar(p.id, fecha).pipe(finalize(()=> this.savingId=null)).subscribe({
      next: upd => {
        const i = this.page?.content.findIndex(x=>x.id===p.id) ?? -1;
        if (i>=0 && this.page) this.page.content[i] = upd;
        alert('Fecha reprogramada');
      },
      error: e => alert('Error: ' + (e?.error?.message ?? e.message))
    });
  }

  entregar(p: PedidoRow) {
    if (!confirm(`Marcar pedido #${p.id} como ENTREGADO?`)) return;
    this.savingId = p.id;
    this.api.entregar(p.id).pipe(finalize(()=> this.savingId=null)).subscribe({
      next: () => {
        if (this.page) this.page.content = this.page.content.filter(x => x.id !== p.id);
      },
      error: e => alert('Error: ' + (e?.error?.message ?? e.message))
    });
  }
}
