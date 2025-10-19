// src/app/services/moderacion/moderacion.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { ProductoPendienteView } from '../../entities/ProductoPendienteView';

@Injectable({ providedIn: 'root' })
export class ModeracionService {
  private base = `${environment.apiUrl}/moderador/revisiones`;
  constructor(private http: HttpClient) {}

  pendientes() {
    return this.http.get<ProductoPendienteView[]>(`${this.base}/pendientes`);
  }
  aprobar(productoId: number) {
    return this.http.post(`${this.base}/${productoId}/aprobar`, {});
  }
  rechazar(productoId: number, comentario?: string) {
    return this.http.post(`${this.base}/${productoId}/rechazar`, { comentario });
  }
}
