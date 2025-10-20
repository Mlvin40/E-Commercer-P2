import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { ProductosService } from '../../../services/productos/productos.service';

@Component({
  standalone: true,
  selector: 'app-mis-productos',
  imports: [CommonModule, FormsModule],
  templateUrl: './mis-productos.component.html'
})
export class MisProductosComponent implements OnInit {
  items: any[] = [];
  cargando = false; error: string|null = null; exito: string|null = null;

  // para edición inline sencilla
  edit: any|null = null;

  estados = ['NUEVO','USADO'];
  categorias = ['TECNOLOGIA','HOGAR','ACADEMICO','PERSONAL','DECORACION','OTRO'];

  constructor(private api: ProductosService) {}

  ngOnInit() { this.load(); }

  load() {
    this.cargando = true; this.error = null; this.exito = null;
    this.api.misProductos().subscribe({
      next: res => { this.items = res; this.cargando = false; },
      error: e => { this.error = e?.error?.message || 'Error cargando'; this.cargando = false; }
    });
  }

  empezarEditar(p: any) { this.edit = { ...p }; }
  cancelarEditar() { this.edit = null; }

  guardar(f: NgForm) {
    if (!this.edit) return;
    if (!f.valid) return;
    this.api.actualizar(this.edit.id, {
      nombre: this.edit.nombre,
      descripcion: this.edit.descripcion,
      imagenUrl: this.edit.imagenUrl,
      precio: this.edit.precio,
      stock: this.edit.stock,
      estado: this.edit.estado,
      categoria: this.edit.categoria
    }).subscribe({
      next: () => { this.exito = 'Actualizado (enviado a revisión)'; this.edit = null; this.load(); },
      error: e => { this.error = e?.error?.message || 'No se pudo actualizar'; }
    });
  }

  eliminar(id: number) {
    if (!confirm('¿Eliminar este producto?')) return;
    this.api.eliminar(id).subscribe({
      next: () => this.load(),
      error: e => this.error = e?.error?.message || 'No se pudo eliminar'
    });
  }
}
