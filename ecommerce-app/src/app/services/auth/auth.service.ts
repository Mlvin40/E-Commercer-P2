import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { environment } from '../../../environments/environment';
import { tap } from 'rxjs/operators';
import { AuthResponse } from '../../entities/AuthResponse';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private base = environment.apiUrl;

  constructor(private http: HttpClient, private router: Router) {}

  register(data: { nombre: string; correo: string; password: string; }) {
    return this.http.post<void>(`${this.base}/auth/register`, data);
  }

  login(data: { correo: string; password: string; }) {
    return this.http.post<AuthResponse>(`${this.base}/auth/login`, data).pipe(
      tap(res => {
        localStorage.setItem('access', res.accessToken);
        if (res.refreshToken) localStorage.setItem('refresh', res.refreshToken);
      })
    );
  }

  // redirige al portal de login al cerrar sesión
  logout(redirectTo: string = '/login') {
    localStorage.removeItem('access');
    localStorage.removeItem('refresh');
    this.router.navigateByUrl(redirectTo, { replaceUrl: true });
  }

  get isLoggedIn(): boolean { return !!localStorage.getItem('access'); }

  get role(): string | null {
    const t = localStorage.getItem('access'); if (!t) return null;
    try { return JSON.parse(atob(t.split('.')[1]))['role'] || null; } catch { return null; }
  }

  get isExpired(): boolean {
    const t = localStorage.getItem('access'); if (!t) return false;
    try {
      const { exp } = JSON.parse(atob(t.split('.')[1]));
      return exp ? Math.floor(Date.now()/1000) >= exp : false;
    } catch { return false; }
  }
}
