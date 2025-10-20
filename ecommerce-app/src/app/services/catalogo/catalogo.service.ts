import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { ProductoCard } from '../../entities/ProductoCard';
import { Page } from '../../entities/Page';
import { AuthService } from '../../services/auth/auth.service';

@Injectable({ providedIn: 'root' })
export class CatalogoService {
  private pub = `${environment.apiUrl}/public/catalogo`;
  private priv = `${environment.apiUrl}/comun/catalogo`;

  constructor(private http: HttpClient, private auth: AuthService) {}

  /*-si no hay login -> público (todos aprobados)
   *-si hay login comun -> privado (aprobados EXCLUYENDO los míos)
   */
  list(page = 0, size = 12, categoria?: string, q?: string) {
    const baseUrl = this.auth.isLoggedIn ? this.priv : this.pub;

    let params = new HttpParams().set('page', page).set('size', size);
    if (categoria) params = params.set('categoria', categoria);
    if (q) params = params.set('q', q);

    return this.http.get<Page<ProductoCard>>(baseUrl, { params });
  }

  // detalle público
  detalle(id: number) {
    return this.http.get<any>(`${environment.apiUrl}/public/productos/${id}`);
  }
}
