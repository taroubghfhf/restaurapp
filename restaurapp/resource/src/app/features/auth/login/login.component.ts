import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService, NotificationService } from '../../../services';
import { LoadingComponent } from '../../../shared/components';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, LoadingComponent],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  private authService = inject(AuthService);
  private notificationService = inject(NotificationService);
  private router = inject(Router);
  
  email = signal('');
  password = signal('');
  isLoading = signal(false);
  showPassword = signal(false);
  
  // Credenciales de ejemplo para mostrar
  exampleCredentials = [
    { role: 'Admin', email: 'admin@restaurapp.com', password: 'admin123' },
    { role: 'Mesero', email: 'juan.perez@restaurapp.com', password: 'waiter123' },
    { role: 'Chef', email: 'maria.gonzalez@restaurapp.com', password: 'chef123' }
  ];

  /**
   * Maneja el envío del formulario de login
   */
  onLogin(): void {
    if (!this.email() || !this.password()) {
      this.notificationService.warning('Por favor complete todos los campos');
      return;
    }

    this.isLoading.set(true);
    
    this.authService.login({
      email: this.email(),
      password: this.password()
    }).subscribe({
      next: (response) => {
        this.isLoading.set(false);
        this.notificationService.success(`¡Bienvenido, ${response.name}!`);
        
        // Redirigir según el rol del usuario
        this.redirectByRole(response.role);
      },
      error: (error) => {
        this.isLoading.set(false);
        console.error('Error en login:', error);
        
        if (error.status === 401) {
          this.notificationService.error('Credenciales inválidas. Verifique su email y contraseña.');
        } else if (error.status === 0) {
          this.notificationService.error('No se puede conectar al servidor. Verifique que el backend esté ejecutándose.');
        } else {
          this.notificationService.error('Error al iniciar sesión. Intente nuevamente.');
        }
      }
    });
  }

  /**
   * Redirige al usuario según su rol
   */
  private redirectByRole(role: string): void {
    switch (role) {
      case 'Admin':
        this.router.navigate(['/admin/dashboard']);
        break;
      case 'Waiter':
        this.router.navigate(['/waiter/orders']);
        break;
      case 'Chef':
        this.router.navigate(['/chef/orders']);
        break;
      default:
        this.router.navigate(['/']);
    }
  }

  /**
   * Usa credenciales de ejemplo
   */
  useExampleCredentials(example: { email: string; password: string }): void {
    this.email.set(example.email);
    this.password.set(example.password);
    this.notificationService.info(`Credenciales de ${example.email} cargadas`);
  }

  /**
   * Alterna la visibilidad de la contraseña
   */
  togglePasswordVisibility(): void {
    this.showPassword.update(value => !value);
  }
}

