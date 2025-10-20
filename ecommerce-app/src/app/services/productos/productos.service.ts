import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { ProductoUpsert } from '../../entities/ProductoUpsert';

@Injectable({ providedIn: 'root' })
export class ProductosService {
  private base = environment.apiUrl;
  constructor(private http: HttpClient) { }

  crear(dto: ProductoUpsert) {
    // protegida por JWT → tu interceptor ya agrega el Authorization
    return this.http.post<{ id: number }>(`${this.base}/comun/productos`, dto);
  }
  misProductos() {
    return this.http.get<any[]>(`${this.base}/comun/mis-productos`);
  }
  actualizar(id: number, body: any) {
    return this.http.put<void>(`${this.base}/comun/mis-productos/${id}`, body);
  }
  eliminar(id: number) {
    return this.http.delete<void>(`${this.base}/comun/mis-productos/${id}`);
  }
}
