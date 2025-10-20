// catalogo.component.ts (añade control para comentarios y estrellas)
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CatalogoService } from '../../../services/catalogo/catalogo.service';
import { ProductoCard } from '../../../entities/ProductoCard';
import { CarritoService } from '../../../services/carrito/carrito.service';
import { RatingsPublicService, ComentarioView } from '../../../services/ratings-public/ratings-public.service';

@Component({
  standalone: true,
  selector: 'app-catalogo',
  imports: [CommonModule, FormsModule],
  templateUrl: './catalogo.component.html'
})
export class CatalogoComponent implements OnInit {
  items: ProductoCard[] = [];
  cargando = false;
  error: string | null = null;

  page = 0;
  size = 12;
  totalPages = 0;

  categoria = '';
  q = '';

  categorias = ['TECNOLOGIA','HOGAR','ACADEMICO','PERSONAL','DECORACION','OTRO'];

  // comentarios por producto
  expandedComments: Record<number, boolean> = {};
  comentarios: Record<number, ComentarioView[] | undefined> = {};
  cargandoComentarios: Record<number, boolean> = {};

  constructor(
    private api: CatalogoService,
    private cart: CarritoService,
    private ratingsPublic: RatingsPublicService
  ) {}

  ngOnInit() { this.load(); }

  get pages(): number[] {
    return Array.from({ length: this.totalPages }, (_, i) => i);
  }

  addToCart(id: number) {
    this.cart.add(id, 1).subscribe({
      next: () => alert('Agregado al carrito'),
      error: e => alert(e?.error?.message || 'No se pudo agregar')
    });
  }

  load(p: number = 0) {
    this.cargando = true; this.error = null;
    this.api.list(p, this.size, this.categoria || undefined, this.q || undefined)
      .subscribe({
        next: res => {
          this.items = res.content;
          this.page = res.number;
          this.totalPages = res.totalPages;
          this.cargando = false;
        },
        error: e => {
          this.error = e?.error?.message || 'Error cargando catálogo';
          this.cargando = false;
        }
      });
  }

  buscar() { this.load(0); }
  setCategoria(c: string) { this.categoria = c; this.load(0); }
  clearFiltros() { this.categoria = ''; this.q = ''; this.load(0); }

  toggleComentarios(productoId: number) {
    this.expandedComments[productoId] = !this.expandedComments[productoId];
    if (this.expandedComments[productoId] && this.comentarios[productoId] == null) {
      this.cargandoComentarios[productoId] = true;
      this.ratingsPublic.listar(productoId).subscribe({
        next: list => { this.comentarios[productoId] = list; this.cargandoComentarios[productoId] = false; },
        error: _ => { this.comentarios[productoId] = []; this.cargandoComentarios[productoId] = false; }
      });
    }
  }

  // muestra "★ ★ ★ ☆ ☆  (4.2 / 37)"
  starsLine(avg?: number): string {
    const a = Math.round((avg || 0) * 2) / 2; // redondeo a 0.5
    const full = Math.floor(a);
    const half = (a - full) >= 0.5 ? 1 : 0;
    const empty = 5 - full - half;
    return '★'.repeat(full) + (half ? '½' : '') + '☆'.repeat(empty);
  }
}
