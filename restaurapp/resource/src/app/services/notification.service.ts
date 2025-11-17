import { Injectable } from '@angular/core';
import { Subject, Observable } from 'rxjs';

export interface Notification {
  type: 'success' | 'error' | 'warning' | 'info';
  message: string;
  duration?: number; // en milisegundos, default 3000
}

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private notificationSubject = new Subject<Notification>();
  
  /**
   * Observable para suscribirse a las notificaciones
   */
  get notifications$(): Observable<Notification> {
    return this.notificationSubject.asObservable();
  }

  /**
   * Muestra una notificación de éxito
   */
  success(message: string, duration: number = 3000): void {
    this.show({ type: 'success', message, duration });
  }

  /**
   * Muestra una notificación de error
   */
  error(message: string, duration: number = 5000): void {
    this.show({ type: 'error', message, duration });
  }

  /**
   * Muestra una notificación de advertencia
   */
  warning(message: string, duration: number = 4000): void {
    this.show({ type: 'warning', message, duration });
  }

  /**
   * Muestra una notificación de información
   */
  info(message: string, duration: number = 3000): void {
    this.show({ type: 'info', message, duration });
  }

  /**
   * Muestra una notificación personalizada
   */
  show(notification: Notification): void {
    this.notificationSubject.next(notification);
  }

  /**
   * Limpia todas las notificaciones
   */
  clear(): void {
    // Puede ser implementado según las necesidades
  }
}

