// src/app/services/moderacion/moderacion.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { ProductoPendienteView } from '../../entities/ProductoPendienteView';
import { SancionView } from '../../entities/SancionView';

@Injectable({ providedIn: 'root' })
export class ModeracionService {
  private baseRev = `${environment.apiUrl}/moderador/revisiones`;
  private baseSan = `${environment.apiUrl}/moderador/sanciones`;
  private baseProd = `${environment.apiUrl}/moderador/productos`;


  constructor(private http: HttpClient) {}

  // Revisiones (ya lo tienes)
  pendientes() { return this.http.get<ProductoPendienteView[]>(`${this.baseRev}/pendientes`); }
  aprobar(productoId: number) { return this.http.post(`${this.baseRev}/${productoId}/aprobar`, {}); }
  rechazar(productoId: number, comentario?: string) { return this.http.post(`${this.baseRev}/${productoId}/rechazar`, { comentario }); }

  // Retirar publicado
  retirarPublicado(productoId: number, comentario?: string) {
    return this.http.post(`${this.baseProd}/${productoId}/retirar`, comentario ? { comentario } : {});
  }

  // Sanciones
  crearSancion(usuarioId: number, motivo: string) {
    return this.http.post<{id:number, estado:string}>(this.baseSan, { usuarioId, motivo });
  }
  levantarSancion(id: number) {
    return this.http.patch<void>(`${this.baseSan}/${id}/levantar`, {});
  }
  listarSanciones(usuarioId?: number, estado?: 'ACTIVA'|'LEVANTADA') {
    const params: any = {};
    if (usuarioId) params.usuarioId = usuarioId;
    if (estado) params.estado = estado;
    return this.http.get<SancionView[]>(this.baseSan, { params });
  }
  
  listarVendedores(q?: string, page=0, size=50) {
  const params: any = { page, size };
  if (q) params.q = q;
  return this.http.get<{id:number; nombre:string; correo:string}[]>(
    `${this.baseSan.replace('/sanciones','')}/usuarios/vendedores`, { params }
  );
}

}
