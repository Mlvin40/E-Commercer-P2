import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import { RegistroDto } from '../../entities/RegistroDto'; 
import { AuthResponse } from '../../entities/AuthResponse';
import { LoginData } from '../../entities/LoginData';

@Injectable({ providedIn: 'root' })
export class UsuarioService {
  private baseUrl = environment.apiUrl; // p.ej. http://localhost:8080/api

  constructor(private http: HttpClient) {}

  registrar(data: RegistroDto): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/auth/register`, data);
  }

    login(data: LoginData): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.baseUrl}/auth/login`, data)
      .pipe(
        tap(res => {
          // guarda tokens; los usarás luego en el interceptor
          localStorage.setItem('access', res.accessToken);
          localStorage.setItem('refresh', res.refreshToken);
        })
      );
  }

}
