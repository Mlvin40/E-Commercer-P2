import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CatalogoService } from '../../../services/catalogo/catalogo.service';
import { ProductoCard } from '../../../entities/ProductoCard';
import { CarritoService } from '../../../services/carrito/carrito.service';

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

  // IMPORTANTE: usa modificador de acceso en el carrito
  constructor(private api: CatalogoService, private cart: CarritoService) {}

  ngOnInit() {
    this.load();
  }

  // helper para la paginación en el template
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
}
