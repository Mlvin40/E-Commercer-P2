import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

export interface Tarjeta {
  id: number;
  last4: string;
  marca: string;
  titular: string;
  predeterminada: boolean;
}

export interface CheckoutRequest {
  savedCardId?: number;
  numero?: string;
  expMes?: string;
  expAnio?: string;
  titular?: string;
  guardar?: boolean;
}

export interface CheckoutResponse {
  pedidoId: number;
  total: number;
  estadoPago: string;
}

@Injectable({ providedIn: 'root' })
export class CheckoutService {
  private base = `${environment.apiUrl}/comun/checkout`;
  constructor(private http: HttpClient) {}

  tarjetas() { return this.http.get<Tarjeta[]>(`${this.base}/tarjetas`); }
  pagar(body: CheckoutRequest) { return this.http.post<CheckoutResponse>(this.base, body); }
}
