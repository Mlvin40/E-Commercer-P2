import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export type RolEmpleado = 'MODERADOR' | 'LOGISTICA' | 'ADMIN';

export interface EmpleadoView {
  id: number;
  nombre: string;
  correo: string;
  rol: RolEmpleado;
  activo: boolean;
  fechaCreacion: string; 
}

export interface CrearEmpleadoRequest {
  nombre: string;
  correo: string;
  password: string;
  rol: RolEmpleado;
}

export interface ActualizarEmpleadoRequest {
  nombre?: string;
  correo?: string;
  password?: string;
  rol?: RolEmpleado;
  activo?: boolean;
}

export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}

@Injectable({ providedIn: 'root' })
export class AdminUsuariosService {
  private base = `${environment.apiUrl}/admin/usuarios`;

  constructor(private http: HttpClient) {}

  listar(q = '', page = 0, size = 20): Observable<Page<EmpleadoView>> {
    let params = new HttpParams().set('page', page).set('size', size);
    if (q?.trim()) params = params.set('q', q.trim());
    return this.http.get<Page<EmpleadoView>>(this.base, { params });
  }

  crear(body: CrearEmpleadoRequest): Observable<EmpleadoView> {
    return this.http.post<EmpleadoView>(this.base, body);
  }

  actualizar(id: number, body: ActualizarEmpleadoRequest): Observable<EmpleadoView> {
    return this.http.patch<EmpleadoView>(`${this.base}/${id}`, body);
  }
}
