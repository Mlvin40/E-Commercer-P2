import { HttpInterceptorFn } from '@angular/common/http';
import { environment } from '../../environments/environment';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const token = localStorage.getItem('access'); // JWT token
  const isAuthEndpoint =
    req.url.startsWith(`${environment.apiUrl}/auth/`); // Esto es para no agregar el token en las llamadas de autenticación

  req = req.clone({ setHeaders: { "ngrok-skip-browser-warning": "true" } }); // Agregar el encabezado para omitir la advertencia de ngrok
  if (token && !isAuthEndpoint) {
    req = req.clone({ setHeaders: { Authorization: `Bearer ${token}` } });
  }
  return next(req);
};
