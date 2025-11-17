import { inject } from '@angular/core';
import { Router, CanActivateFn, ActivatedRouteSnapshot } from '@angular/router';
import { AuthService } from '../../services';

/**
 * Guard para proteger rutas basadas en roles
 * Uso: canActivate: [roleGuard], data: { roles: ['Admin', 'Manager'] }
 */
export const roleGuard: CanActivateFn = (route: ActivatedRouteSnapshot) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  // Verificar si está autenticado
  if (!authService.isAuthenticated()) {
    router.navigate(['/login']);
    return false;
  }

  // Obtener los roles permitidos desde la configuración de la ruta
  const allowedRoles = route.data['roles'] as string[];
  
  if (!allowedRoles || allowedRoles.length === 0) {
    return true; // No hay restricción de roles
  }

  // Verificar si el usuario tiene alguno de los roles permitidos
  const userRole = authService.getUserRole();
  
  if (userRole && allowedRoles.includes(userRole)) {
    return true;
  }

  // Redirigir a una página de acceso denegado o home
  router.navigate(['/unauthorized']);
  return false;
};
