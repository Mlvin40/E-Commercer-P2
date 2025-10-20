import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CatalogoService} from '../../../services/catalogo/catalogo.service';
import { ProductoCard } from '../../../entities/ProductoCard';

@Component({
  standalone: true,
  selector: 'app-catalogo',
  imports: [CommonModule, FormsModule],
  templateUrl: './catalogo.component.html'
})
export class CatalogoComponent implements OnInit {
  items: ProductoCard[] = [];
  cargando = false; error: string|null = null;
  page = 0; size = 12; totalPages = 0;
  categoria = ''; q = '';

  categorias = ['TECNOLOGIA','HOGAR','ACADEMICO','PERSONAL','DECORACION','OTRO'];

  constructor(private api: CatalogoService) {}

  ngOnInit() { this.load(); }

  load(p: number = 0) {
    this.cargando = true; this.error = null;
    this.api.list(p, this.size, this.categoria || undefined, this.q || undefined)
      .subscribe({
        next: res => { this.items = res.content; this.page = res.number; this.totalPages = res.totalPages; this.cargando = false; },
        error: e => { this.error = e?.error?.message || 'Error cargando catálogo'; this.cargando = false; }
      });
  }

  buscar() { this.load(0); }
  setCategoria(c: string) { this.categoria = c; this.load(0); }
  clearFiltros() { this.categoria = ''; this.q = ''; this.load(0); }
}
