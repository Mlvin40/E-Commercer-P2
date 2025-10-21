// src/app/services/admin-reportes/admin-reportes.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../../environments/environment';

export interface TopProductoRow {
  productoId: number;
  nombre: string;
  totalUnidades: number;
  totalVentas: string; // llega como string por numeric; conviértelo si quieres
}

export interface TopClienteFeeRow {
  usuarioId: number;
  nombre: string;
  correo: string;
  totalGananciaSitio: string; // numeric
  totalGastado: string;       // numeric
  pedidos: number;
}

@Injectable({ providedIn: 'root' })
export class AdminReportesService {
  private base = `${environment.apiUrl}/admin/reportes`;
  constructor(private http: HttpClient) {}

  topProductos(desde: string, hasta: string, limit = 10) {
    const params = new HttpParams()
      .set('desde', desde)
      .set('hasta', hasta)
      .set('limit', limit);
    return this.http.get<TopProductoRow[]>(`${this.base}/top-productos`, { params });
  }

  topClientesFee(desde: string, hasta: string, limit = 5) {
    const params = new HttpParams()
      .set('desde', desde)
      .set('hasta', hasta)
      .set('limit', limit);
    return this.http.get<TopClienteFeeRow[]>(`${this.base}/top-clientes-ganancia`, { params });
  }
}
