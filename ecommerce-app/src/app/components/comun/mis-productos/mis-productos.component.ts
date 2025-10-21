// components/comun/mis-productos/mis-productos.component.ts
import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { finalize, of, switchMap } from 'rxjs';
import { ProductosService } from '../../../services/productos/productos.service';
import { UploadService } from '../../../services/files/upload.service';

@Component({
  standalone: true,
  selector: 'app-mis-productos',
  imports: [CommonModule, FormsModule],
  templateUrl: './mis-productos.component.html'
})
export class MisProductosComponent implements OnInit, OnDestroy {
  items: any[] = [];
  cargando = false; error: string|null = null; exito: string|null = null;

  edit: any|null = null;

  // NUEVO: manejo de archivo y preview
  archivo: File | null = null;
  previewUrl: string | null = null;

  estados = ['NUEVO','USADO'];
  categorias = ['TECNOLOGIA','HOGAR','ACADEMICO','PERSONAL','DECORACION','OTRO'];

  constructor(
    private api: ProductosService,
    private upload: UploadService
  ) {}

  ngOnInit() { this.load(); }

  ngOnDestroy(): void {
    if (this.previewUrl) URL.revokeObjectURL(this.previewUrl);
  }

  load() {
    this.cargando = true; this.error = null; this.exito = null;
    this.api.misProductos().subscribe({
      next: res => { this.items = res; this.cargando = false; },
      error: e => { this.error = e?.error?.message || 'Error cargando'; this.cargando = false; }
    });
  }

  empezarEditar(p: any) {
    this.edit = { ...p };
    // reset de selección de archivo
    this.archivo = null;
    if (this.previewUrl) { URL.revokeObjectURL(this.previewUrl); this.previewUrl = null; }
  }

  cancelarEditar() {
    this.edit = null;
    this.archivo = null;
    if (this.previewUrl) { URL.revokeObjectURL(this.previewUrl); this.previewUrl = null; }
  }

  // NUEVO: selección de archivo
  onFileSelected(ev: Event) {
    const input = ev.target as HTMLInputElement;
    const file = input.files?.[0] || null;

    this.archivo = file;
    this.error = null;

    // preview
    if (this.previewUrl) { URL.revokeObjectURL(this.previewUrl); this.previewUrl = null; }
    if (file) {
      if (!file.type.startsWith('image/')) {
        this.error = 'Selecciona una imagen válida.';
        this.archivo = null;
        return;
      }
      this.previewUrl = URL.createObjectURL(file);
    }
  }

  guardar(f: NgForm) {
    if (!this.edit) return;
    if (!f.valid) return;

    this.exito = null; this.error = null;

    // Subir imagen solo si se seleccionó una nueva
    const subir$ = this.archivo ? this.upload.uploadImage(this.archivo) : of('');

    this.cargando = true;
    subir$
      .pipe(
        switchMap((url) => {
          const imagenUrlFinal = url || this.edit.imagenUrl || '';

          const body = {
            nombre: this.edit.nombre,
            descripcion: this.edit.descripcion,
            imagenUrl: imagenUrlFinal,     // <- si hubo upload, va la nueva URL
            precio: this.edit.precio,
            stock: this.edit.stock,
            estado: this.edit.estado,
            categoria: this.edit.categoria
          };

          return this.api.actualizar(this.edit.id, body);
        }),
        finalize(() => this.cargando = false)
      )
      .subscribe({
        next: () => {
          this.exito = 'Actualizado (enviado a revisión)';
          this.cancelarEditar();
          this.load();
        },
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
