import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

export interface PedidoListItem {
  id: number;
  fecha: string;
  fechaEstimadaEntrega: string;
  estado: string;
  total: number;
}

export interface PedidoItemParaRating {
  productoId: number;
  nombre: string;
  imagenUrl: string;
  cantidad: number;
  precioUnitario: number;
  yaCalificado: boolean;
}

@Injectable({ providedIn: 'root' })
export class PedidosService {
  private base = `${environment.apiUrl}/comun/pedidos`;
  constructor(private http: HttpClient) {}

  listar() { return this.http.get<PedidoListItem[]>(this.base); }
  detalle(id: number) { return this.http.get<PedidoItemParaRating[]>(`${this.base}/${id}`); }
}
