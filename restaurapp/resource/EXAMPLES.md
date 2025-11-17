# Ejemplos de Uso - RestaurApp Angular

Este documento contiene ejemplos completos de componentes y uso de los servicios de RestaurApp.

## Ejemplo 1: Componente de Login

```typescript
import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService, NotificationService } from '../services';
import { LoadingComponent } from '../shared/components';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, LoadingComponent],
  template: `
    <div class="login-container">
      <div class="login-card">
        <h1>RestaurApp</h1>
        <h2>Iniciar Sesión</h2>
        
        <form (ngSubmit)="onLogin()" #loginForm="ngForm">
          <div class="form-group">
            <label for="email">Email</label>
            <input 
              type="email" 
              id="email" 
              name="email"
              [(ngModel)]="credentials.email" 
              required
              placeholder="usuario@restaurapp.com">
          </div>
          
          <div class="form-group">
            <label for="password">Contraseña</label>
            <input 
              type="password" 
              id="password" 
              name="password"
              [(ngModel)]="credentials.password" 
              required
              placeholder="••••••••">
          </div>
          
          <button 
            type="submit" 
            [disabled]="!loginForm.valid || isLoading"
            class="btn-primary">
            {{ isLoading ? 'Iniciando sesión...' : 'Iniciar Sesión' }}
          </button>
        </form>
        
        <app-loading [isLoading]="isLoading" message="Iniciando sesión..."></app-loading>
      </div>
    </div>
  `,
  styles: [`
    .login-container {
      display: flex;
      justify-content: center;
      align-items: center;
      min-height: 100vh;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    }
    
    .login-card {
      background: white;
      padding: 40px;
      border-radius: 12px;
      box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
      width: 100%;
      max-width: 400px;
    }
    
    h1 {
      color: #667eea;
      text-align: center;
      margin-bottom: 10px;
      font-size: 32px;
    }
    
    h2 {
      text-align: center;
      margin-bottom: 30px;
      color: #4a5568;
      font-size: 20px;
    }
    
    .form-group {
      margin-bottom: 20px;
    }
    
    label {
      display: block;
      margin-bottom: 8px;
      color: #4a5568;
      font-weight: 500;
    }
    
    input {
      width: 100%;
      padding: 12px;
      border: 1px solid #e2e8f0;
      border-radius: 6px;
      font-size: 14px;
      transition: border-color 0.3s;
    }
    
    input:focus {
      outline: none;
      border-color: #667eea;
    }
    
    .btn-primary {
      width: 100%;
      padding: 12px;
      background: #667eea;
      color: white;
      border: none;
      border-radius: 6px;
      font-size: 16px;
      font-weight: 500;
      cursor: pointer;
      transition: background 0.3s;
    }
    
    .btn-primary:hover:not(:disabled) {
      background: #5568d3;
    }
    
    .btn-primary:disabled {
      opacity: 0.6;
      cursor: not-allowed;
    }
  `]
})
export class LoginComponent {
  private authService = inject(AuthService);
  private notificationService = inject(NotificationService);
  private router = inject(Router);
  
  credentials = {
    email: '',
    password: ''
  };
  
  isLoading = false;

  onLogin(): void {
    this.isLoading = true;
    
    this.authService.login(this.credentials).subscribe({
      next: (response) => {
        this.isLoading = false;
        this.notificationService.success(`¡Bienvenido, ${response.name}!`);
        
        // Redirigir según el rol
        switch (response.role) {
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
      },
      error: (error) => {
        this.isLoading = false;
        this.notificationService.error('Credenciales inválidas. Intente nuevamente.');
        console.error('Error en login:', error);
      }
    });
  }
}
```

## Ejemplo 2: Lista de Productos con CRUD

```typescript
import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ProductService, CategoryService, NotificationService } from '../services';
import { Product, Category } from '../shared/models';
import { LoadingComponent } from '../shared/components';

@Component({
  selector: 'app-products',
  standalone: true,
  imports: [CommonModule, FormsModule, LoadingComponent],
  template: `
    <div class="products-container">
      <div class="header">
        <h1>Gestión de Productos</h1>
        <button (click)="showCreateForm = true" class="btn-primary">
          + Nuevo Producto
        </button>
      </div>

      <app-loading [isLoading]="isLoading" [fullscreen]="false"></app-loading>

      <!-- Formulario de Creación/Edición -->
      <div *ngIf="showCreateForm" class="modal">
        <div class="modal-content">
          <div class="modal-header">
            <h2>{{ editingProduct ? 'Editar Producto' : 'Nuevo Producto' }}</h2>
            <button (click)="cancelForm()" class="btn-close">×</button>
          </div>
          
          <form (ngSubmit)="saveProduct()" #productForm="ngForm">
            <div class="form-group">
              <label>Nombre</label>
              <input type="text" [(ngModel)]="currentProduct.name" name="name" required>
            </div>
            
            <div class="form-group">
              <label>Categoría</label>
              <select [(ngModel)]="currentProduct.category.categoryId" name="category" required>
                <option [value]="null">Seleccione una categoría</option>
                <option *ngFor="let cat of categories" [value]="cat.categoryId">
                  {{ cat.name }}
                </option>
              </select>
            </div>
            
            <div class="form-group">
              <label>Precio</label>
              <input type="number" [(ngModel)]="currentProduct.price" name="price" required>
            </div>
            
            <div class="form-group">
              <label>Stock</label>
              <input type="number" [(ngModel)]="currentProduct.stock" name="stock" required>
            </div>
            
            <div class="form-group">
              <label>
                <input type="checkbox" [(ngModel)]="currentProduct.status" name="status">
                Disponible
              </label>
            </div>
            
            <div class="form-actions">
              <button type="button" (click)="cancelForm()" class="btn-secondary">
                Cancelar
              </button>
              <button type="submit" [disabled]="!productForm.valid" class="btn-primary">
                Guardar
              </button>
            </div>
          </form>
        </div>
      </div>

      <!-- Lista de Productos -->
      <div class="products-grid">
        <div *ngFor="let product of products" class="product-card">
          <div class="product-header">
            <h3>{{ product.name }}</h3>
            <span 
              class="status-badge" 
              [class.active]="product.status"
              [class.inactive]="!product.status">
              {{ product.status ? 'Disponible' : 'No disponible' }}
            </span>
          </div>
          
          <div class="product-body">
            <p><strong>Categoría:</strong> {{ product.category.name }}</p>
            <p><strong>Precio:</strong> ${{ product.price | number }}</p>
            <p><strong>Stock:</strong> {{ product.stock }}</p>
          </div>
          
          <div class="product-actions">
            <button (click)="editProduct(product)" class="btn-edit">Editar</button>
            <button (click)="deleteProduct(product)" class="btn-delete">Eliminar</button>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .products-container {
      padding: 20px;
      max-width: 1200px;
      margin: 0 auto;
    }
    
    .header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 30px;
    }
    
    .products-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
      gap: 20px;
    }
    
    .product-card {
      background: white;
      border-radius: 8px;
      padding: 20px;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    }
    
    .product-header {
      display: flex;
      justify-content: space-between;
      align-items: start;
      margin-bottom: 15px;
    }
    
    .status-badge {
      padding: 4px 12px;
      border-radius: 12px;
      font-size: 12px;
      font-weight: 500;
    }
    
    .status-badge.active {
      background: #d1fae5;
      color: #065f46;
    }
    
    .status-badge.inactive {
      background: #fee2e2;
      color: #991b1b;
    }
    
    .product-actions {
      display: flex;
      gap: 10px;
      margin-top: 15px;
    }
    
    .btn-edit {
      flex: 1;
      padding: 8px;
      background: #3b82f6;
      color: white;
      border: none;
      border-radius: 4px;
      cursor: pointer;
    }
    
    .btn-delete {
      flex: 1;
      padding: 8px;
      background: #ef4444;
      color: white;
      border: none;
      border-radius: 4px;
      cursor: pointer;
    }
    
    .modal {
      position: fixed;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      background: rgba(0, 0, 0, 0.5);
      display: flex;
      justify-content: center;
      align-items: center;
      z-index: 1000;
    }
    
    .modal-content {
      background: white;
      padding: 30px;
      border-radius: 12px;
      width: 90%;
      max-width: 500px;
    }
    
    .form-group {
      margin-bottom: 20px;
    }
    
    .form-group label {
      display: block;
      margin-bottom: 8px;
      font-weight: 500;
    }
    
    .form-group input,
    .form-group select {
      width: 100%;
      padding: 10px;
      border: 1px solid #e2e8f0;
      border-radius: 4px;
    }
    
    .form-actions {
      display: flex;
      gap: 10px;
      margin-top: 20px;
    }
  `]
})
export class ProductsComponent implements OnInit {
  private productService = inject(ProductService);
  private categoryService = inject(CategoryService);
  private notificationService = inject(NotificationService);
  
  products: Product[] = [];
  categories: Category[] = [];
  showCreateForm = false;
  editingProduct = false;
  isLoading = false;
  
  currentProduct: Product = this.getEmptyProduct();

  ngOnInit(): void {
    this.loadProducts();
    this.loadCategories();
  }

  loadProducts(): void {
    this.isLoading = true;
    this.productService.getAll().subscribe({
      next: (products) => {
        this.products = products;
        this.isLoading = false;
      },
      error: (error) => {
        this.notificationService.error('Error al cargar productos');
        this.isLoading = false;
        console.error(error);
      }
    });
  }

  loadCategories(): void {
    this.categoryService.getAll().subscribe({
      next: (categories) => {
        this.categories = categories;
      },
      error: (error) => {
        this.notificationService.error('Error al cargar categorías');
        console.error(error);
      }
    });
  }

  editProduct(product: Product): void {
    this.currentProduct = { ...product };
    this.editingProduct = true;
    this.showCreateForm = true;
  }

  deleteProduct(product: Product): void {
    if (confirm(`¿Está seguro de eliminar ${product.name}?`)) {
      this.productService.delete(product.productId!).subscribe({
        next: () => {
          this.notificationService.success('Producto eliminado exitosamente');
          this.loadProducts();
        },
        error: (error) => {
          this.notificationService.error('Error al eliminar producto');
          console.error(error);
        }
      });
    }
  }

  saveProduct(): void {
    const category = this.categories.find(
      c => c.categoryId === this.currentProduct.category.categoryId
    );
    
    if (category) {
      this.currentProduct.category = category;
    }

    const operation = this.editingProduct
      ? this.productService.update(this.currentProduct)
      : this.productService.create(this.currentProduct);

    operation.subscribe({
      next: () => {
        this.notificationService.success(
          this.editingProduct ? 'Producto actualizado' : 'Producto creado'
        );
        this.cancelForm();
        this.loadProducts();
      },
      error: (error) => {
        this.notificationService.error('Error al guardar producto');
        console.error(error);
      }
    });
  }

  cancelForm(): void {
    this.showCreateForm = false;
    this.editingProduct = false;
    this.currentProduct = this.getEmptyProduct();
  }

  getEmptyProduct(): Product {
    return {
      name: '',
      category: { categoryId: undefined, name: '' },
      price: 0,
      stock: 0,
      status: true
    };
  }
}
```

## Ejemplo 3: Panel de Pedidos para Chef (con WebSocket)

```typescript
import { Component, OnInit, OnDestroy, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OrderTicketService, WebSocketService, NotificationService } from '../services';
import { OrderTicket } from '../shared/models';

@Component({
  selector: 'app-chef-orders',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="orders-container">
      <h1>Pedidos - Cocina</h1>
      
      <div class="orders-columns">
        <div class="orders-column">
          <h2>Pendientes ({{ pendingOrders.length }})</h2>
          <div *ngFor="let order of pendingOrders" class="order-card pending">
            <div class="order-header">
              <span class="order-id">#{{ order.orderTicketId }}</span>
              <span class="order-time">{{ order.date | date:'short' }}</span>
            </div>
            <div class="order-body">
              <p><strong>Mesa:</strong> {{ order.table.location }}</p>
              <p><strong>Mesero:</strong> {{ order.waiter.name }}</p>
            </div>
            <button (click)="startOrder(order)" class="btn-start">
              Iniciar Preparación
            </button>
          </div>
        </div>
        
        <div class="orders-column">
          <h2>En Preparación ({{ inProgressOrders.length }})</h2>
          <div *ngFor="let order of inProgressOrders" class="order-card in-progress">
            <div class="order-header">
              <span class="order-id">#{{ order.orderTicketId }}</span>
              <span class="order-time">{{ order.date | date:'short' }}</span>
            </div>
            <div class="order-body">
              <p><strong>Mesa:</strong> {{ order.table.location }}</p>
              <p><strong>Mesero:</strong> {{ order.waiter.name }}</p>
            </div>
            <button (click)="completeOrder(order)" class="btn-complete">
              Marcar como Listo
            </button>
          </div>
        </div>
        
        <div class="orders-column">
          <h2>Completados ({{ completedOrders.length }})</h2>
          <div *ngFor="let order of completedOrders" class="order-card completed">
            <div class="order-header">
              <span class="order-id">#{{ order.orderTicketId }}</span>
              <span class="order-time">{{ order.date | date:'short' }}</span>
            </div>
            <div class="order-body">
              <p><strong>Mesa:</strong> {{ order.table.location }}</p>
              <p><strong>Mesero:</strong> {{ order.waiter.name }}</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .orders-container {
      padding: 20px;
    }
    
    .orders-columns {
      display: grid;
      grid-template-columns: repeat(3, 1fr);
      gap: 20px;
      margin-top: 20px;
    }
    
    .orders-column h2 {
      margin-bottom: 15px;
      font-size: 18px;
    }
    
    .order-card {
      background: white;
      border-radius: 8px;
      padding: 15px;
      margin-bottom: 15px;
      border-left: 4px solid;
    }
    
    .order-card.pending {
      border-left-color: #f59e0b;
    }
    
    .order-card.in-progress {
      border-left-color: #3b82f6;
    }
    
    .order-card.completed {
      border-left-color: #10b981;
    }
    
    .order-header {
      display: flex;
      justify-content: space-between;
      margin-bottom: 10px;
    }
    
    .order-id {
      font-weight: bold;
      font-size: 18px;
    }
    
    .btn-start {
      width: 100%;
      padding: 10px;
      background: #3b82f6;
      color: white;
      border: none;
      border-radius: 4px;
      margin-top: 10px;
      cursor: pointer;
    }
    
    .btn-complete {
      width: 100%;
      padding: 10px;
      background: #10b981;
      color: white;
      border: none;
      border-radius: 4px;
      margin-top: 10px;
      cursor: pointer;
    }
  `]
})
export class ChefOrdersComponent implements OnInit, OnDestroy {
  private orderService = inject(OrderTicketService);
  private wsService = inject(WebSocketService);
  private notificationService = inject(NotificationService);
  
  pendingOrders: OrderTicket[] = [];
  inProgressOrders: OrderTicket[] = [];
  completedOrders: OrderTicket[] = [];
  
  // IDs de estados (estos deberían venir de un servicio o constantes)
  PENDING_STATUS_ID = 1;
  IN_PROGRESS_STATUS_ID = 2;
  COMPLETED_STATUS_ID = 3;

  ngOnInit(): void {
    this.loadOrders();
    this.connectWebSocket();
  }

  ngOnDestroy(): void {
    this.wsService.disconnect();
  }

  loadOrders(): void {
    // Cargar pedidos pendientes
    this.orderService.getByStatus(this.PENDING_STATUS_ID).subscribe({
      next: (orders) => {
        this.pendingOrders = orders;
      }
    });

    // Cargar pedidos en progreso
    this.orderService.getByStatus(this.IN_PROGRESS_STATUS_ID).subscribe({
      next: (orders) => {
        this.inProgressOrders = orders;
      }
    });

    // Cargar pedidos completados
    this.orderService.getByStatus(this.COMPLETED_STATUS_ID).subscribe({
      next: (orders) => {
        this.completedOrders = orders;
      }
    });
  }

  connectWebSocket(): void {
    this.wsService.connect('ws://localhost:8080/ws');
    
    this.wsService.messages$.subscribe({
      next: (message) => {
        console.log('Mensaje WebSocket:', message);
        // Actualizar pedidos cuando hay cambios
        this.loadOrders();
        this.notificationService.info('Nuevo pedido recibido');
      }
    });
  }

  startOrder(order: OrderTicket): void {
    this.orderService.updateStatus(order.orderTicketId!, this.IN_PROGRESS_STATUS_ID).subscribe({
      next: () => {
        this.notificationService.success('Pedido iniciado');
        this.loadOrders();
      },
      error: (error) => {
        this.notificationService.error('Error al iniciar pedido');
        console.error(error);
      }
    });
  }

  completeOrder(order: OrderTicket): void {
    this.orderService.updateStatus(order.orderTicketId!, this.COMPLETED_STATUS_ID).subscribe({
      next: () => {
        this.notificationService.success('Pedido completado');
        this.loadOrders();
      },
      error: (error) => {
        this.notificationService.error('Error al completar pedido');
        console.error(error);
      }
    });
  }
}
```

## Ejemplo 4: Configuración de Rutas con Guards

```typescript
// app.routes.ts
import { Routes } from '@angular/router';
import { authGuard, roleGuard } from './core/guards';

export const routes: Routes = [
  {
    path: 'login',
    loadComponent: () => import('./pages/login/login.component').then(m => m.LoginComponent)
  },
  {
    path: 'admin',
    canActivate: [authGuard, roleGuard],
    data: { roles: ['Admin'] },
    children: [
      {
        path: 'dashboard',
        loadComponent: () => import('./pages/admin/dashboard.component').then(m => m.DashboardComponent)
      },
      {
        path: 'products',
        loadComponent: () => import('./pages/admin/products.component').then(m => m.ProductsComponent)
      },
      {
        path: 'users',
        loadComponent: () => import('./pages/admin/users.component').then(m => m.UsersComponent)
      }
    ]
  },
  {
    path: 'waiter',
    canActivate: [authGuard, roleGuard],
    data: { roles: ['Admin', 'Waiter'] },
    children: [
      {
        path: 'orders',
        loadComponent: () => import('./pages/waiter/orders.component').then(m => m.WaiterOrdersComponent)
      },
      {
        path: 'tables',
        loadComponent: () => import('./pages/waiter/tables.component').then(m => m.TablesComponent)
      }
    ]
  },
  {
    path: 'chef',
    canActivate: [authGuard, roleGuard],
    data: { roles: ['Admin', 'Chef'] },
    children: [
      {
        path: 'orders',
        loadComponent: () => import('./pages/chef/orders.component').then(m => m.ChefOrdersComponent)
      }
    ]
  },
  {
    path: 'unauthorized',
    loadComponent: () => import('./pages/unauthorized/unauthorized.component').then(m => m.UnauthorizedComponent)
  },
  {
    path: '',
    redirectTo: '/login',
    pathMatch: 'full'
  }
];
```

## Uso del Componente de Notificaciones

Para usar el componente de notificaciones, agrégalo en tu `app.ts`:

```typescript
import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { NotificationComponent } from './shared/components';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, NotificationComponent],
  template: `
    <router-outlet></router-outlet>
    <app-notification></app-notification>
  `
})
export class App {}
```

Luego puedes usar el servicio en cualquier componente:

```typescript
constructor(private notificationService: NotificationService) {}

someMethod() {
  this.notificationService.success('Operación exitosa');
  this.notificationService.error('Ocurrió un error');
  this.notificationService.warning('Advertencia');
  this.notificationService.info('Información');
}
```

## Notas Finales

- Todos los componentes usan `standalone: true` siguiendo las mejores prácticas de Angular 20
- Se usa el patrón de inyección con `inject()` en lugar de constructores
- Los servicios manejan errores con notificaciones visuales
- El WebSocket permite actualizaciones en tiempo real
- Los guards protegen las rutas según autenticación y roles

