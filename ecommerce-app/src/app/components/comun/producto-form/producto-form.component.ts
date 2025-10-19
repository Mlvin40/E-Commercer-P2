import { Component, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { switchMap, finalize, of } from 'rxjs';
import { ProductosService } from '../../../services/productos/productos.service';
import { UploadService } from '../../../services/files/upload.service';
import { ProductoUpsert } from '../../../entities/ProductoUpsert';

@Component({
  standalone: true,
  selector: 'app-producto-form',
  imports: [CommonModule, FormsModule],
  templateUrl: './producto-form.component.html',
  styleUrls: ['./producto-form.component.scss']
})
export class ProductoFormComponent implements OnDestroy {
  m: ProductoUpsert = {
    nombre: '',
    descripcion: '',
    imagenUrl: '',   // aquí guardaremos la URL devuelta por el backend
    precio: 0,
    stock: 1,
    estado: 'NUEVO',
    categoria: 'OTRO'
  };

  archivo: File | null = null;
  previewUrl: string | null = null;

  cargando = false;
  exito: string | null = null;
  error: string | null = null;

  estados = ['NUEVO', 'USADO'];
  categorias = ['TECNOLOGIA', 'HOGAR', 'ACADEMICO', 'PERSONAL', 'DECORACION', 'OTRO'];

  constructor(
    private productos: ProductosService,
    private upload: UploadService
  ) {}

  onFileSelected(ev: Event) {
    const input = ev.target as HTMLInputElement;
    const file = input.files?.[0] || null;

    this.archivo = file;
    this.error = null;

    // preview (opcional)
    if (this.previewUrl) {
      URL.revokeObjectURL(this.previewUrl);
      this.previewUrl = null;
    }
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
    this.error = this.exito = null;

    if (!f.valid || this.m.precio <= 0 || this.m.stock < 1) {
      this.error = 'Revisa los datos: precio > 0 y stock ≥ 1.';
      return;
    }

    this.cargando = true;

    // Flujo:
    // Si hay archivo → subir → tomar URL → setear en m.imagenUrl → crear producto
    // Si NO hay archivo pero el usuario pegó una URL en imagenUrl → crear directo
    // Si no hay nada, crear sin imagen.
    const subir$ = this.archivo ? this.upload.uploadImage(this.archivo) : of(this.m.imagenUrl || '');

    subir$
      .pipe(
        switchMap((url) => {
          // si vino URL de upload, úsala
          if (url) this.m.imagenUrl = url;
          return this.productos.crear(this.m);
        }),
        finalize(() => (this.cargando = false))
      )
      .subscribe({
        next: () => {
          this.exito = 'Producto enviado a revisión.';
          f.resetForm({ estado: 'NUEVO', categoria: 'OTRO', stock: 1, precio: 0 });
          this.archivo = null;
          if (this.previewUrl) {
            URL.revokeObjectURL(this.previewUrl);
            this.previewUrl = null;
          }
        },
        error: (e) => {
          this.error = e?.error?.message || 'No se pudo publicar.';
        }
      });
  }

  ngOnDestroy(): void {
    if (this.previewUrl) URL.revokeObjectURL(this.previewUrl);
  }
}
