# RestaurApp - Frontend Angular 20

🍽️ Aplicación frontend para el sistema de gestión de restaurantes RestaurApp, construida con Angular 20 en su versión más reciente.

## 📋 Descripción

Este proyecto Angular proporciona una interfaz completa y moderna para consumir todos los endpoints del backend de RestaurApp. Está organizado siguiendo la arquitectura **Core-Services-Shared**, garantizando escalabilidad y mantenibilidad.

## 🏗️ Arquitectura del Proyecto

```
src/app/
├── core/                           # Funcionalidades fundamentales
│   ├── guards/                     # Guards de autenticación y roles
│   │   ├── auth.guard.ts          # Protección de rutas autenticadas
│   │   ├── role.guard.ts          # Protección basada en roles
│   │   └── index.ts
│   └── interceptors/              # Interceptores HTTP
│       └── auth.interceptor.ts    # Inyección automática del token JWT
│
├── services/                      # Servicios para consumir API
│   ├── auth.service.ts           # Autenticación y gestión de sesión
│   ├── role.service.ts           # Gestión de roles
│   ├── status.service.ts         # Gestión de estados
│   ├── user.service.ts           # Gestión de usuarios
│   ├── category.service.ts       # Gestión de categorías
│   ├── product.service.ts        # Gestión de productos
│   ├── table.service.ts          # Gestión de mesas
│   ├── order-ticket.service.ts   # Gestión de pedidos
│   ├── order-item.service.ts     # Gestión de items de pedidos
│   ├── notification.service.ts   # Sistema de notificaciones
│   ├── websocket.service.ts      # Comunicación en tiempo real
│   └── index.ts                  # Barrel export
│
└── shared/                        # Recursos compartidos
    ├── components/                # Componentes reutilizables
    │   ├── notification/          # Componente de notificaciones toast
    │   ├── loading/              # Componente de carga/spinner
    │   └── index.ts
    ├── constants/                 # Constantes y configuración
    │   └── api.constants.ts      # URLs y endpoints de la API
    └── models/                    # Interfaces y modelos TypeScript
        ├── role.model.ts
        ├── status.model.ts
        ├── user.model.ts
        ├── category.model.ts
        ├── product.model.ts
        ├── table.model.ts
        ├── order-ticket.model.ts
        ├── order-item.model.ts
        └── index.ts              # Barrel export
```

## ✨ Características Principales

### 🔐 Autenticación y Autorización
- **AuthService**: Gestión completa de login/logout con almacenamiento de tokens
- **AuthGuard**: Protección de rutas que requieren autenticación
- **RoleGuard**: Control de acceso basado en roles (Admin, Waiter, Chef)
- **AuthInterceptor**: Inyección automática de token JWT en todas las peticiones

### 🔌 Servicios CRUD Completos
Cada entidad del backend tiene su servicio correspondiente con operaciones CRUD:

- **RoleService**: Gestión de roles del sistema
- **StatusService**: Gestión de estados (pedidos, mesas)
- **UserService**: Gestión de usuarios
- **CategoryService**: Gestión de categorías de productos
- **ProductService**: Gestión de productos del menú
- **TableService**: Gestión de mesas del restaurante
- **OrderTicketService**: Gestión completa de pedidos (incluye filtros por estado, mesero, chef)
- **OrderItemService**: Gestión de items individuales de pedidos

### 🔔 Sistema de Notificaciones
- **NotificationService**: Servicio para mostrar notificaciones toast
- **NotificationComponent**: Componente visual con 4 tipos de notificaciones:
  - ✅ Success (verde)
  - ❌ Error (rojo)
  - ⚠️ Warning (amarillo)
  - ℹ️ Info (azul)

### ⚡ Comunicación en Tiempo Real
- **WebSocketService**: Conexión WebSocket para actualizaciones en tiempo real
- Soporte para notificaciones de cambios en pedidos
- Reconexión automática en caso de pérdida de conexión

### 🎨 Componentes Reutilizables
- **LoadingComponent**: Spinner de carga con modo fullscreen opcional
- **NotificationComponent**: Sistema de notificaciones toast
- Todos los componentes son **standalone** (Angular 20)

## 🚀 Instalación y Configuración

### Prerrequisitos
- Node.js >= 18.x
- Angular CLI >= 20.x
- Backend RestaurApp ejecutándose en `http://localhost:8080`

### Instalación

```bash
# Navegar a la carpeta del proyecto
cd resource

# Instalar dependencias
npm install

# Verificar instalación
ng version
```

### Configuración de la API

Si tu backend está en una URL diferente, actualiza la configuración en:

**`src/app/shared/constants/api.constants.ts`:**
```typescript
export const API_CONFIG = {
  baseUrl: 'http://localhost:8080',  // Cambia esta URL si es necesario
  endpoints: {
    auth: '/auth',
    role: '/role',
    status: '/status',
    user: '/user',
    category: '/category',
    product: '/product',
    table: '/table',
    orderTicket: '/order-ticket',
    orderItem: '/order-item'
  }
};
```

## 🏃‍♂️ Ejecución

### Modo Desarrollo
```bash
npm start
# o
ng serve
```

La aplicación estará disponible en: `http://localhost:4200`

### Build de Producción
```bash
npm run build
# o
ng build --configuration production
```

Los archivos compilados estarán en `dist/`

### Modo Watch (desarrollo con recarga automática)
```bash
npm run watch
```

## 📚 Documentación

### Guías Disponibles

1. **[SERVICES.md](./SERVICES.md)** - Documentación completa de todos los servicios
   - Descripción de cada servicio
   - Métodos disponibles
   - Ejemplos de uso
   - Endpoints del backend
   - Modelos de datos

2. **[EXAMPLES.md](./EXAMPLES.md)** - Ejemplos prácticos completos
   - Componente de Login
   - CRUD de Productos
   - Panel de Pedidos para Chef
   - Configuración de rutas con guards
   - Uso de notificaciones

### Uso Rápido de Servicios

#### 1. Autenticación

```typescript
import { Component, inject } from '@angular/core';
import { AuthService } from './services';

@Component({...})
export class LoginComponent {
  private authService = inject(AuthService);

  login() {
    const credentials = { email: 'admin@restaurapp.com', password: 'admin123' };
    
    this.authService.login(credentials).subscribe({
      next: (response) => {
        console.log('Login exitoso:', response);
        // Token se guarda automáticamente en localStorage
        // Redirigir según el rol: response.role
      },
      error: (error) => console.error('Error:', error)
    });
  }
}
```

#### 2. CRUD de Productos

```typescript
import { Component, inject } from '@angular/core';
import { ProductService } from './services';

@Component({...})
export class ProductsComponent {
  private productService = inject(ProductService);

  // Listar productos
  loadProducts() {
    this.productService.getAll().subscribe(products => {
      console.log('Productos:', products);
    });
  }

  // Crear producto
  createProduct() {
    const newProduct = {
      name: 'Caesar Salad',
      category: { categoryId: 1 },
      price: 8500,
      stock: 50,
      status: true
    };
    
    this.productService.create(newProduct).subscribe(product => {
      console.log('Producto creado:', product);
    });
  }

  // Actualizar producto
  updateProduct(product) {
    this.productService.update(product).subscribe(updated => {
      console.log('Producto actualizado:', updated);
    });
  }

  // Eliminar producto
  deleteProduct(productId: number) {
    this.productService.delete(productId).subscribe(() => {
      console.log('Producto eliminado');
    });
  }
}
```

#### 3. Gestión de Pedidos

```typescript
import { Component, inject } from '@angular/core';
import { OrderTicketService } from './services';

@Component({...})
export class OrdersComponent {
  private orderService = inject(OrderTicketService);

  // Obtener todos los pedidos
  getAllOrders() {
    this.orderService.getAll().subscribe(orders => {
      console.log('Pedidos:', orders);
    });
  }

  // Filtrar por estado
  getOrdersByStatus(statusId: number) {
    this.orderService.getByStatus(statusId).subscribe(orders => {
      console.log('Pedidos con estado:', orders);
    });
  }

  // Actualizar estado del pedido
  updateOrderStatus(orderId: number, statusId: number) {
    this.orderService.updateStatus(orderId, statusId).subscribe(order => {
      console.log('Estado actualizado:', order);
    });
  }

  // Obtener pedidos del mesero actual
  getMyOrders(waiterId: number) {
    this.orderService.getByWaiter(waiterId).subscribe(orders => {
      console.log('Mis pedidos:', orders);
    });
  }
}
```

### Protección de Rutas

#### Ruta protegida por autenticación:
```typescript
{
  path: 'dashboard',
  component: DashboardComponent,
  canActivate: [authGuard]
}
```

#### Ruta protegida por rol:
```typescript
{
  path: 'admin',
  component: AdminComponent,
  canActivate: [authGuard, roleGuard],
  data: { roles: ['Admin'] }
}
```

#### Múltiples roles permitidos:
```typescript
{
  path: 'orders',
  component: OrdersComponent,
  canActivate: [authGuard, roleGuard],
  data: { roles: ['Admin', 'Waiter'] }
}
```

## 🎯 Endpoints del Backend Soportados

### Autenticación
- `POST /auth/login` - Login de usuario

### Roles
- `GET /role` - Listar roles
- `POST /role` - Crear rol
- `PUT /role` - Actualizar rol
- `DELETE /role` - Eliminar rol

### Estados
- `GET /status` - Listar estados
- `POST /status` - Crear estado
- `PUT /status` - Actualizar estado
- `DELETE /status` - Eliminar estado

### Usuarios
- `GET /user` - Listar usuarios
- `POST /user` - Crear usuario
- `DELETE /user` - Eliminar usuario

### Categorías
- `GET /category` - Listar categorías
- `POST /category` - Crear categoría
- `PUT /category` - Actualizar categoría
- `DELETE /category` - Eliminar categoría

### Productos
- `GET /product` - Listar productos
- `POST /product` - Crear producto
- `PUT /product` - Actualizar producto
- `DELETE /product` - Eliminar producto

### Mesas
- `GET /table` - Listar mesas
- `POST /table` - Crear mesa
- `PUT /table` - Actualizar mesa
- `DELETE /table` - Eliminar mesa

### Pedidos (Order Tickets)
- `GET /order-ticket` - Listar todos los pedidos
- `POST /order-ticket` - Crear pedido
- `PUT /order-ticket` - Actualizar pedido completo
- `PATCH /order-ticket/{id}/status/{statusId}` - Actualizar solo estado
- `DELETE /order-ticket` - Eliminar pedido
- `GET /order-ticket/status/{statusId}` - Filtrar por estado
- `GET /order-ticket/waiter/{waiterId}` - Filtrar por mesero
- `GET /order-ticket/chef/{chefId}` - Filtrar por chef
- `GET /order-ticket/{id}/items` - Obtener items del pedido

### Items de Pedidos
- `GET /order-item` - Listar items
- `POST /order-item` - Crear item
- `PUT /order-item` - Actualizar item
- `DELETE /order-item` - Eliminar item

## 🔧 Tecnologías Utilizadas

- **Angular 20.3.0** - Framework principal
- **TypeScript 5.9.2** - Lenguaje de programación
- **RxJS 7.8.0** - Programación reactiva
- **HttpClient** - Peticiones HTTP
- **Router** - Navegación y guards
- **Standalone Components** - Arquitectura moderna de Angular

## 📦 Estructura de Modelos

### Role
```typescript
interface Role {
  roleId?: number;
  name: string;
}
```

### User
```typescript
interface User {
  userId?: number;
  name: string;
  email: string;
  password?: string;
  role: Role;
}
```

### Product
```typescript
interface Product {
  productId?: number;
  name: string;
  category: Category;
  price: number;
  stock: number;
  status: boolean;
}
```

### OrderTicket
```typescript
interface OrderTicket {
  orderTicketId?: number;
  date: string;
  table: Table;
  waiter: User;
  chef: User;
  status: Status;
}
```

> Ver todos los modelos en `src/app/shared/models/`

## 🎨 Convenciones de Código

- **Componentes Standalone**: Todos los componentes usan `standalone: true`
- **Inyección de Dependencias**: Usar `inject()` en lugar de constructores
- **Naming**: PascalCase para clases, camelCase para métodos y variables
- **Imports**: Usar barrel exports (`index.ts`) para importaciones limpias
- **Servicios**: Todos con `providedIn: 'root'`
- **Observable Naming**: Terminar con `$` (ejemplo: `orders$`)

## 🚦 Flujo de Trabajo

1. **Login** → AuthService guarda token en localStorage
2. **AuthInterceptor** → Agrega token automáticamente a todas las peticiones
3. **Guards** → Protegen rutas según autenticación y rol
4. **Services** → Consumen endpoints del backend
5. **Components** → Usan servicios para mostrar/manipular datos
6. **Notifications** → Feedback visual al usuario

## 🐛 Manejo de Errores

Todos los servicios implementan manejo de errores con notificaciones:

```typescript
this.productService.getAll().subscribe({
  next: (products) => {
    // Éxito
    this.products = products;
  },
  error: (error) => {
    // Error
    this.notificationService.error('Error al cargar productos');
    console.error(error);
  }
});
```

## 🔄 Próximas Mejoras

- [ ] Implementar paginación en listas
- [ ] Agregar filtros y búsqueda avanzada
- [ ] Caché de datos estáticos (roles, categorías)
- [ ] Modo offline con Service Workers
- [ ] Internacionalización (i18n)
- [ ] Testing unitario y e2e
- [ ] Dark mode
- [ ] PWA support

## 📝 Scripts Disponibles

```bash
npm start          # Inicia servidor de desarrollo
npm run build      # Build de producción
npm run watch      # Build con watch mode
npm test           # Ejecuta tests unitarios
ng generate        # Genera componentes, servicios, etc.
```

## 🤝 Contribución

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📄 Licencia

Este proyecto es parte del sistema RestaurApp.

## 📞 Contacto

Para más información, consulta la colección de Postman del backend: `RestaurAppv1.postman_collection.json`

---

**Desarrollado con Angular 20** 🚀
