import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../services';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <div class="dashboard-container">
      <div class="dashboard-header">
        <h1>🎉 ¡Bienvenido a RestaurApp!</h1>
        <p class="welcome-message">Has iniciado sesión exitosamente</p>
      </div>

      <div class="user-info">
        <div class="info-card">
          <h2>Información del Usuario</h2>
          <div class="info-item">
            <span class="label">👤 Nombre:</span>
            <span class="value">{{ userName }}</span>
          </div>
          <div class="info-item">
            <span class="label">📧 ID:</span>
            <span class="value">{{ userId }}</span>
          </div>
          <div class="info-item">
            <span class="label">🎭 Rol:</span>
            <span class="value role-badge">{{ userRole }}</span>
          </div>
        </div>
      </div>

      <!-- Panel de Administración (solo para Admin) -->
      <div class="admin-panel" *ngIf="isAdmin()">
        <h3>⚙️ Panel de Administración</h3>
        <div class="admin-cards">
          <a routerLink="/admin/roles" class="admin-card">
            <div class="card-icon">👥</div>
            <div class="card-content">
              <h4>Gestión de Roles</h4>
              <p>Crear, editar y eliminar roles</p>
            </div>
          </a>
          <a routerLink="/admin/status" class="admin-card">
            <div class="card-icon">🎯</div>
            <div class="card-content">
              <h4>Gestión de Estados</h4>
              <p>Administrar estados del sistema</p>
            </div>
          </a>
          <a routerLink="/admin/users" class="admin-card">
            <div class="card-icon">👤</div>
            <div class="card-content">
              <h4>Gestión de Usuarios</h4>
              <p>Administrar usuarios y roles</p>
            </div>
          </a>
          <a routerLink="/admin/categories" class="admin-card">
            <div class="card-icon">📦</div>
            <div class="card-content">
              <h4>Gestión de Categorías</h4>
              <p>Clasificar productos del menú</p>
            </div>
          </a>
          <a routerLink="/admin/products" class="admin-card">
            <div class="card-icon">🍽️</div>
            <div class="card-content">
              <h4>Gestión de Productos</h4>
              <p>Administrar menú del restaurante</p>
            </div>
          </a>
          <a routerLink="/admin/tables" class="admin-card">
            <div class="card-icon">🪑</div>
            <div class="card-content">
              <h4>Gestión de Mesas</h4>
              <p>Administrar mesas del restaurante</p>
            </div>
          </a>
          <a routerLink="/admin/order-tickets" class="admin-card">
            <div class="card-icon">📋</div>
            <div class="card-content">
              <h4>Gestión de Órdenes</h4>
              <p>Órdenes del restaurante</p>
            </div>
          </a>
          <a routerLink="/admin/order-items" class="admin-card">
            <div class="card-icon">🍽️</div>
            <div class="card-content">
              <h4>Gestión de Items</h4>
              <p>Productos en cada orden</p>
            </div>
          </a>
          <a routerLink="/admin/websocket-monitor" class="admin-card">
            <div class="card-icon">🔌</div>
            <div class="card-content">
              <h4>Monitor WebSocket</h4>
              <p>Conexión en tiempo real</p>
            </div>
          </a>
        </div>
      </div>

      <div class="actions">
        <button (click)="logout()" class="btn-logout">
          🚪 Cerrar Sesión
        </button>
      </div>

      <div class="info-section">
        <h3>📚 Próximos Pasos:</h3>
        <ul>
          <li>Explora los servicios disponibles en la carpeta <code>services/</code></li>
          <li>Revisa los ejemplos en <code>EXAMPLES.md</code></li>
          <li>Consulta la documentación en <code>SERVICES.md</code></li>
          <li>Crea tus propios componentes usando los servicios</li>
        </ul>
      </div>
    </div>
  `,
  styles: [`
    .dashboard-container {
      min-height: 100vh;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      padding: 40px 20px;
    }

    .dashboard-header {
      text-align: center;
      color: white;
      margin-bottom: 40px;

      h1 {
        font-size: 2.5rem;
        margin: 0 0 10px 0;
      }

      .welcome-message {
        font-size: 1.2rem;
        opacity: 0.9;
      }
    }

    .user-info {
      max-width: 600px;
      margin: 0 auto 30px;
    }

    .info-card {
      background: white;
      border-radius: 15px;
      padding: 30px;
      box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);

      h2 {
        color: #2d3748;
        margin: 0 0 20px 0;
      }

      .info-item {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 12px 0;
        border-bottom: 1px solid #e2e8f0;

        &:last-child {
          border-bottom: none;
        }

        .label {
          color: #4a5568;
          font-weight: 500;
        }

        .value {
          color: #2d3748;
          font-weight: 600;

          &.role-badge {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 6px 16px;
            border-radius: 20px;
            font-size: 0.9rem;
          }
        }
      }
    }

    .actions {
      text-align: center;
      margin: 30px 0;

      .btn-logout {
        background: white;
        color: #e53e3e;
        border: 2px solid #e53e3e;
        padding: 14px 40px;
        border-radius: 10px;
        font-size: 1.1rem;
        font-weight: 600;
        cursor: pointer;
        transition: all 0.3s ease;

        &:hover {
          background: #e53e3e;
          color: white;
          transform: translateY(-2px);
          box-shadow: 0 5px 15px rgba(229, 62, 62, 0.4);
        }
      }
    }

    .info-section {
      max-width: 800px;
      margin: 40px auto;
      background: white;
      padding: 30px;
      border-radius: 15px;
      box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);

      h3 {
        color: #2d3748;
        margin: 0 0 20px 0;
      }

      ul {
        list-style: none;
        padding: 0;
        margin: 0;

        li {
          padding: 10px 0;
          color: #4a5568;
          font-size: 1.05rem;
          line-height: 1.6;

          &::before {
            content: '✓';
            color: #48bb78;
            font-weight: bold;
            margin-right: 10px;
          }

          code {
            background: #edf2f7;
            padding: 2px 8px;
            border-radius: 4px;
            color: #667eea;
            font-family: 'Courier New', monospace;
          }
        }
      }
    }

    .admin-panel {
      max-width: 1200px;
      margin: 40px auto;
      background: white;
      padding: 30px;
      border-radius: 15px;
      box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);

      h3 {
        color: #2d3748;
        margin: 0 0 20px 0;
        font-size: 1.5rem;
      }
    }

    .admin-cards {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
      gap: 20px;
      margin-top: 20px;
    }

    .admin-card {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      padding: 25px;
      border-radius: 12px;
      display: flex;
      gap: 20px;
      align-items: center;
      cursor: pointer;
      transition: all 0.3s ease;
      text-decoration: none;
      color: white;
      border: none;

      &:hover:not(.disabled) {
        transform: translateY(-5px);
        box-shadow: 0 10px 25px rgba(102, 126, 234, 0.4);
      }

      &.disabled {
        background: linear-gradient(135deg, #a0aec0 0%, #718096 100%);
        cursor: not-allowed;
        opacity: 0.6;
      }

      .card-icon {
        font-size: 3rem;
        flex-shrink: 0;
      }

      .card-content {
        flex: 1;

        h4 {
          margin: 0 0 8px 0;
          font-size: 1.2rem;
        }

        p {
          margin: 0;
          opacity: 0.9;
          font-size: 0.95rem;
        }
      }
    }

    @media (max-width: 768px) {
      .dashboard-header h1 {
        font-size: 2rem;
      }

      .info-card {
        padding: 20px;
      }

      .admin-cards {
        grid-template-columns: 1fr;
      }

      .admin-card {
        padding: 20px;
      }
    }
  `]
})
export class DashboardComponent {
  private authService = inject(AuthService);
  private router = inject(Router);

  userName = localStorage.getItem('userName') || 'Usuario';
  userId = localStorage.getItem('userId') || 'N/A';
  userRole = localStorage.getItem('userRole') || 'N/A';

  isAdmin(): boolean {
    return this.userRole === 'Admin';
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}

