import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

export interface RatingCreate {
  productoId: number;
  estrellas: number;
  comentario?: string;
}

@Injectable({ providedIn: 'root' })
export class RatingsService {
  private base = `${environment.apiUrl}/comun/ratings`;
  constructor(private http: HttpClient) {}

  crear(body: RatingCreate) {
    return this.http.post<void>(this.base, body);
  }

}
