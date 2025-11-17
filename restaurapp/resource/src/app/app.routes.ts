import { Routes } from '@angular/router';
import { authGuard, roleGuard } from './core/guards';

export const routes: Routes = [
  {
    path: 'login',
    loadComponent: () => import('./features/auth/login/login.component').then(m => m.LoginComponent)
  },
  {
    path: 'dashboard',
    canActivate: [authGuard],
    loadComponent: () => import('./features/auth/dashboard/dashboard.component').then(m => m.DashboardComponent)
  },
  {
    path: 'admin',
    canActivate: [authGuard, roleGuard],
    data: { roles: ['Admin'] },
    children: [
      {
        path: 'dashboard',
        loadComponent: () => import('./features/auth/dashboard/dashboard.component').then(m => m.DashboardComponent)
      },
      {
        path: 'roles',
        loadComponent: () => import('./features/admin/role-management/role-management.component').then(m => m.RoleManagementComponent)
      },
      {
        path: 'status',
        loadComponent: () => import('./features/admin/status-management/status-management.component').then(m => m.StatusManagementComponent)
      },
      {
        path: 'users',
        loadComponent: () => import('./features/admin/user-management/user-management.component').then(m => m.UserManagementComponent)
      },
      {
        path: 'categories',
        loadComponent: () => import('./features/admin/category-management/category-management.component').then(m => m.CategoryManagementComponent)
      },
      {
        path: 'products',
        loadComponent: () => import('./features/admin/product-management/product-management.component').then(m => m.ProductManagementComponent)
      },
      {
        path: 'tables',
        loadComponent: () => import('./features/admin/table-management/table-management.component').then(m => m.TableManagementComponent)
      },
      {
        path: 'order-items',
        loadComponent: () => import('./features/admin/order-item-management/order-item-management.component').then(m => m.OrderItemManagementComponent)
      },
      {
        path: 'order-tickets',
        loadComponent: () => import('./features/admin/order-ticket-management/order-ticket-management.component').then(m => m.OrderTicketManagementComponent)
      },
      {
        path: 'websocket-monitor',
        loadComponent: () => import('./features/admin/websocket-monitor/websocket-monitor.component').then(m => m.WebSocketMonitorComponent)
      }
    ]
  },
  {
    path: 'waiter/orders',
    canActivate: [authGuard],
    loadComponent: () => import('./features/auth/dashboard/dashboard.component').then(m => m.DashboardComponent)
  },
  {
    path: 'chef/orders',
    canActivate: [authGuard],
    loadComponent: () => import('./features/auth/dashboard/dashboard.component').then(m => m.DashboardComponent)
  },
  {
    path: '',
    redirectTo: '/login',
    pathMatch: 'full'
  }
];
