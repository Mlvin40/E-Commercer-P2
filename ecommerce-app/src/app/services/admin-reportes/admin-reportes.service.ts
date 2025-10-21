// src/app/services/admin-reportes/admin-reportes.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../../environments/environment';

export interface TopProductoRow {
  productoId: number;
  nombre: string;
  totalUnidades: number;
  totalVentas: string;
}

export interface TopVendedorGananciaRow {
  vendedorId: number;
  nombre: string;
  correo: string;
  gananciaVendedor: string;
  unidadesVendidas: number;
  ventasBrutas: string;
}

export interface TopVendedorUnidadesRow {
  vendedorId: number;
  nombre: string;
  correo: string;
  unidadesVendidas: number;
  pedidos: number;
  ventasBrutas: string;
}

export interface TopClientePedidosRow {
  clienteId: number;
  nombre: string;
  correo: string;
  pedidos: number;
  totalGastado: string;
}

export interface TopClienteProductosVentaRow {
  vendedorId: number;
  nombre: string;
  correo: string;
  productosEnVenta: number;
  stockTotal: number;
}

export interface Page<T> {
  content: T[];
  number: number;
  size: number;
  totalElements: number;
  totalPages: number;
}

export interface SancionHistRow {
  id: number;
  moderadorId: number;
  moderador: string;
  usuarioId: number;
  usuario: string;
  correo: string;
  motivo: string;
  fecha: string;  // ISO (LocalDate → string)
  estado: string; // ACTIVA | LEVANTADA
}



@Injectable({ providedIn: 'root' })
export class AdminReportesService {
  private base = `${environment.apiUrl}/admin/reportes`;
  constructor(private http: HttpClient) {}

  topProductos(desde: string, hasta: string, top = 10) {
    const params = new HttpParams().set('desde', desde).set('hasta', hasta).set('top', top);
    return this.http.get<TopProductoRow[]>(`${this.base}/top-productos`, { params });
  }

  topVendedoresGanancia(desde: string, hasta: string, top = 5) {
    const params = new HttpParams().set('desde', desde).set('hasta', hasta).set('top', top);
    return this.http.get<TopVendedorGananciaRow[]>(`${this.base}/top-vendedores-ganancia`, { params });
  }

  // rep4
  topClientesVentas(desde: string, hasta: string, top = 5) {
    const params = new HttpParams().set('desde', desde).set('hasta', hasta).set('top', top);
    return this.http.get<TopVendedorUnidadesRow[]>(`${this.base}/top-clientes-ventas`, { params });
  }

  // rep4
  topClientesPedidos(desde: string, hasta: string, top = 10) {
    const params = new HttpParams().set('desde', desde).set('hasta', hasta).set('top', top);
    return this.http.get<TopClientePedidosRow[]>(`${this.base}/top-clientes-pedidos`, { params });
  }

  //rep5
    topClientesProductosVenta(top = 10) {
    const params = new HttpParams().set('top', top);
    return this.http.get<TopClienteProductosVentaRow[]>(
      `${this.base}/top-clientes-productos-venta`,
      { params }
    );
  }

    historialSanciones(paramsIn: {
    estado?: string;
    q?: string;
    desde?: string;
    hasta?: string;     // inclusivo (YYYY-MM-DD)
    page?: number;
    size?: number;
  }) {
    let params = new HttpParams();
    if (paramsIn.estado) params = params.set('estado', paramsIn.estado);
    if (paramsIn.q) params = params.set('q', paramsIn.q);
    if (paramsIn.desde) params = params.set('desde', paramsIn.desde);
    if (paramsIn.hasta) params = params.set('hasta', paramsIn.hasta);
    params = params
      .set('page', String(paramsIn.page ?? 0))
      .set('size', String(paramsIn.size ?? 20));
    return this.http.get<Page<SancionHistRow>>(`${this.base}/historial-sanciones`, { params });
  }


}
