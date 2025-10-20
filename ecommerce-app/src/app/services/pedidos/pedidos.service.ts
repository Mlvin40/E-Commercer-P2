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

@Injectable({ providedIn: 'root' })
export class PedidosService {
  private base = `${environment.apiUrl}/comun/pedidos`;
  constructor(private http: HttpClient) {}
  listar() { return this.http.get<PedidoListItem[]>(this.base); }
}
