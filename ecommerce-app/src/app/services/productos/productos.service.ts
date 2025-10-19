import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { ProductoUpsert } from '../../entities/ProductoUpsert';

@Injectable({ providedIn: 'root' })
export class ProductosService {
  private base = environment.apiUrl;
  constructor(private http: HttpClient) {}

  crear(dto: ProductoUpsert) {
    // protegida por JWT → tu interceptor ya agrega el Authorization
    return this.http.post<{id:number}>(`${this.base}/comun/productos`, dto);
  }
}
