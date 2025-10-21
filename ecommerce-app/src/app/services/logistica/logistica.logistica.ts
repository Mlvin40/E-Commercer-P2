import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface PedidoRow {
  id: number;
  comprador: string;
  total: number;
  fechaPedido: string | null;
  fechaEstimadaEntrega: string; // YYYY-MM-DD
  estado: 'EN_CURSO'|'ENTREGADO';
  items: number;
}

export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}

@Injectable({ providedIn: 'root' })
export class LogisticaService {
  private base = `${environment.apiUrl}/logistica/pedidos`;

  constructor(private http: HttpClient) {}

  listar(page=0, size=20): Observable<Page<PedidoRow>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<Page<PedidoRow>>(this.base, { params });
  }

  reprogramar(id: number, fecha: string) {
    return this.http.patch<PedidoRow>(`${this.base}/${id}/fecha-entrega`, { fechaEstimadaEntrega: fecha });
  }

  entregar(id: number, fecha?: string) {
    return this.http.post<void>(`${this.base}/${id}/entregar`, fecha ? { fechaEntregada: fecha } : {});
  }
}
