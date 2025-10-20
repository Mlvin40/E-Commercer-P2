import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

export interface CarritoItem {
  productoId: number;
  nombre: string;
  imagenUrl: string;
  precio: number;
  cantidad: number;
  stockDisponible: number;
}
export interface CarritoView {
  carritoId: number | null;
  items: CarritoItem[];
  subtotal: number;
}

@Injectable({ providedIn: 'root' })
export class CarritoService {
  
  private base = `${environment.apiUrl}/comun/carrito`;
  constructor(private http: HttpClient) {}

  ver() { return this.http.get<CarritoView>(this.base); }
  add(productoId: number, cantidad = 1) {
    return this.http.post<CarritoView>(`${this.base}/items`, { productoId, cantidad });
  }
  update(productoId: number, cantidad: number) {
    return this.http.put<CarritoView>(`${this.base}/items/${productoId}`, { cantidad });
  }
  remove(productoId: number) {
    return this.http.delete<CarritoView>(`${this.base}/items/${productoId}`);
  }
  clear() {
    return this.http.delete(`${this.base}`, { responseType: 'text' as 'json' });
  }
}
