// pedidos.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PedidosService, PedidoListItem, PedidoItemParaRating } from '../../../services/pedidos/pedidos.service';
import { RatingsService } from '../../../services/ratings/ratings.service';

@Component({
  standalone: true,
  selector: 'app-pedidos',
  imports: [CommonModule, FormsModule],
  templateUrl: './pedidos.component.html'
})
export class PedidosComponent implements OnInit {
  items: PedidoListItem[] = [];
  detalle: Record<number, PedidoItemParaRating[]> = {};
  expanded: number | null = null;

  cargando=false; error:string|null=null;
  enviando=false;

  // estado de formularios por ítem (clave: productoId)
  estrellas: Record<number, number> = {};
  comentario: Record<number, string> = {};

  constructor(private api: PedidosService, private ratings: RatingsService) {}

  ngOnInit() { this.load(); }

  load() {
    this.cargando = true; this.error = null;
    this.api.listar().subscribe({
      next: r => { this.items = r; this.cargando = false; },
      error: e => { this.error = e?.error?.message || 'Error'; this.cargando = false; }
    });
  }

  toggle(pedidoId: number) {
    this.expanded = (this.expanded === pedidoId ? null : pedidoId);
    if (this.expanded && !this.detalle[pedidoId]) {
      this.api.detalle(pedidoId).subscribe({
        next: d => {
          this.detalle[pedidoId] = d;
          // valores por defecto para estrellas
          d.forEach(it => { if (this.estrellas[it.productoId] == null) this.estrellas[it.productoId] = 5; });
        },
        error: e => alert(e?.error?.message || 'No se pudo cargar el detalle')
      });
    }
  }

  calificar(productoId: number) {
    const estrellas = this.estrellas[productoId];
    const comentario = (this.comentario[productoId] || '').trim() || undefined;

    this.enviando = true;
    this.ratings.crear({ productoId, estrellas, comentario }).subscribe({
      next: () => {
        this.enviando = false;
        // marca como calificado en la UI
        for (const pid of Object.keys(this.detalle)) {
          this.detalle[+pid] = this.detalle[+pid].map(it =>
            it.productoId === productoId ? { ...it, yaCalificado: true } : it
          );
        }
        alert('¡Gracias por tu calificación!');
      },
      error: e => { this.enviando = false; alert(e?.error?.message || 'No se pudo calificar'); }
    });
  }
}
