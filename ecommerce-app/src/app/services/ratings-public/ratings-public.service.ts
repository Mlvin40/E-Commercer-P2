import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

export interface ComentarioView {
  usuario: string;
  estrellas: number;
  comentario: string;
  fecha: string; 
}

@Injectable({ providedIn: 'root' })
export class RatingsPublicService {
  constructor(private http: HttpClient) {}
  listar(productoId: number) {
    return this.http.get<ComentarioView[]>(`${environment.apiUrl}/public/productos/${productoId}/comentarios`);
    // OJO: environment.apiUrl ya incluye /api
  }
}
