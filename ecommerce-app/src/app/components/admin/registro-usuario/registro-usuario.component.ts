import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {
  AdminUsuariosService,
  CrearEmpleadoRequest,
  ActualizarEmpleadoRequest,
  EmpleadoView,
  Page,
  RolEmpleado
} from '../../../services/admin-usuarios/admin-usuarios.service';
import { finalize } from 'rxjs/operators';

@Component({
  selector: 'app-registro-usuario',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './registro-usuario.component.html',
  styleUrls: ['./registro-usuario.component.scss']
})
export class RegistroUsuarioComponent implements OnInit {
  // --- Registro ---
  nuevo: CrearEmpleadoRequest = { nombre: '', correo: '', password: '', rol: 'LOGISTICA' };
  creando = false;
  roles: RolEmpleado[] = ['MODERADOR','LOGISTICA','ADMIN'];

  // --- Lista / búsqueda / edición ---
  q = '';
  page: Page<EmpleadoView> | null = null;
  loading = false;
  savingId: number | null = null;

  // buffer de edición por fila (id -> cambios) — NO incluye 'rol'
  edit: Record<number, ActualizarEmpleadoRequest> = {};

  constructor(private api: AdminUsuariosService) {}

  ngOnInit(): void { this.load(); }

  // Cargar página
  load(p = 0): void {
    this.loading = true;
    this.api.listar(this.q, p, 20)
      .pipe(finalize(() => this.loading = false))
      .subscribe({
        next: res => this.page = res,
        error: e => alert('No se pudo cargar: ' + (e?.error?.message ?? e.message))
      });
  }

  // Crear empleado
  crear(): void {
    if (!this.nuevo.nombre || !this.nuevo.correo || !this.nuevo.password) {
      alert('Completa nombre, correo y password.');
      return;
    }
    this.creando = true;
    this.api.crear(this.nuevo)
      .pipe(finalize(()=> this.creando = false))
      .subscribe({
        next: _ => {
          alert('Empleado creado');
          // limpiar form y refrescar lista (mantiene último rol elegido)
          this.nuevo = { nombre: '', correo: '', password: '', rol: this.nuevo.rol };
          this.load(0);
        },
        error: e => alert('Error al crear: ' + (e?.error?.message ?? e.message))
      });
  }

  // Captura de cambios inline
  setEdit(id: number, campo: keyof ActualizarEmpleadoRequest, valor: any): void {
    this.edit[id] = { ...(this.edit[id] ?? {}), [campo]: valor };
  }

  // Saber si hay cambios para habilitar "Guardar"
  tieneCambios(u: EmpleadoView): boolean {
    const body = this.edit[u.id];
    if (!body) return false;
    return Object.keys(body).some(k => (body as any)[k] !== undefined && (body as any)[k] !== null && (body as any)[k] !== '');
  }

  // Guardar cambios de una fila
  guardar(u: EmpleadoView): void {
    const body = this.edit[u.id];
    if (!body || !this.tieneCambios(u)) {
      alert('No hay cambios que guardar.');
      return;
    }
    this.savingId = u.id;
    this.api.actualizar(u.id, body)
      .pipe(finalize(()=> this.savingId=null))
      .subscribe({
        next: upd => {
          if (!this.page) return;
          const i = this.page.content.findIndex(x => x.id === u.id);
          if (i >= 0) this.page.content[i] = upd;
          delete this.edit[u.id];
          alert('Empleado actualizado');
        },
        error: e => alert('Error al actualizar: ' + (e?.error?.message ?? e.message))
      });
  }

  // Toggle rápido de "activo"
  toggleActivo(u: EmpleadoView): void {
    this.setEdit(u.id, 'activo', !u.activo);
    this.guardar(u);
  }

  // Clase para badge de rol
  roleClass(rol: string): string {
    switch (rol) {
      case 'ADMIN': return 'badge-admin';
      case 'MODERADOR': return 'badge-moderador';
      case 'LOGISTICA': return 'badge-logistica';
      default: return '';
    }
  }

  // Paginación
  go(p: number): void {
    if (!this.page) return;
    if (p < 0 || p >= this.page.totalPages) return;
    this.load(p);
  }

  trackById = (_: number, it: EmpleadoView) => it.id;
}
