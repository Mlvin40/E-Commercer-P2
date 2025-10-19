// core/auth.guard.ts (funcional)
import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth/auth.service';

export const AuthGuard: CanActivateFn = (route) => {
  const auth = inject(AuthService);
  const router = inject(Router);

  // debe estar logueado
  if (!auth.isLoggedIn /* || auth.isExpired */) {
    router.navigate(['/login']);
    return false;
  }

  // comprobar rol solicitado por la ruta
  const requiredRole = route.data?.['role'] as string | undefined;
  if (requiredRole) {
    const myRole = auth.role;
    if (!myRole || myRole !== requiredRole) {
      router.navigate(['/inicio']); // o a una página de "no autorizado"
      return false;
    }
  }

  return true;
};
