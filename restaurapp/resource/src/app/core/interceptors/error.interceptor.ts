import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';

/**
 * Interceptor para manejo global de errores HTTP
 * Maneja errores comunes como 401 (Unauthorized), 403 (Forbidden), etc.
 */
export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      let errorMessage = 'Ha ocurrido un error';

      if (error.error instanceof ErrorEvent) {
        // Error del lado del cliente
        errorMessage = `Error: ${error.error.message}`;
      } else {
        // Error del lado del servidor
        switch (error.status) {
          case 401:
            // No autorizado - redirigir al login
            errorMessage = 'Sesión expirada. Por favor inicie sesión nuevamente.';
            localStorage.removeItem('authToken');
            localStorage.removeItem('userRole');
            localStorage.removeItem('userId');
            localStorage.removeItem('userName');
            router.navigate(['/login']);
            break;
          
          case 403:
            // Prohibido - sin permisos
            errorMessage = 'No tiene permisos para realizar esta acción.';
            break;
          
          case 404:
            // No encontrado
            errorMessage = 'Recurso no encontrado.';
            break;
          
          case 409:
            // Conflicto (ej: registro duplicado)
            errorMessage = error.error?.message || 'El registro ya existe.';
            break;
          
          case 500:
            // Error interno del servidor
            errorMessage = 'Error interno del servidor. Por favor intente más tarde.';
            break;
          
          default:
            errorMessage = error.error?.message || `Error ${error.status}: ${error.statusText}`;
        }
      }

      console.error('Error HTTP:', {
        status: error.status,
        message: errorMessage,
        error: error.error
      });

      // Aquí podrías mostrar un toast/notification con el error
      // Por ejemplo: notificationService.showError(errorMessage);

      return throwError(() => ({
        status: error.status,
        message: errorMessage,
        originalError: error
      }));
    })
  );
};

