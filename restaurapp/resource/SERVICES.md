# RestaurApp - Servicios Angular

## Descripción General

Este proyecto Angular proporciona una interfaz completa para consumir todos los endpoints del backend de RestaurApp. La estructura está organizada en tres capas principales:

- **Core**: Guards, interceptores y funcionalidades fundamentales
- **Services**: Servicios para consumir los endpoints del backend
- **Shared**: Modelos, constantes y utilidades compartidas

## Estructura de Carpetas

```
src/app/
├── core/
│   ├── guards/
│   └── interceptors/
│       └── auth.interceptor.ts
├── services/
│   ├── auth.service.ts
│   ├── role.service.ts
│   ├── status.service.ts
│   ├── user.service.ts
│   ├── category.service.ts
│   ├── product.service.ts
│   ├── table.service.ts
│   ├── order-ticket.service.ts
│   ├── order-item.service.ts
│   └── index.ts
└── shared/
    ├── constants/
    │   └── api.constants.ts
    └── models/
        ├── role.model.ts
        ├── status.model.ts
        ├── user.model.ts
        ├── category.model.ts
        ├── product.model.ts
        ├── table.model.ts
        ├── order-ticket.model.ts
        ├── order-item.model.ts
        └── index.ts
```

## Configuración

### 1. Configuración de HttpClient

El proyecto está configurado para usar `provideHttpClient` con el interceptor de autenticación en `app.config.ts`:

```typescript
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { authInterceptor } from './core/interceptors/auth.interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideHttpClient(withInterceptors([authInterceptor]))
  ]
};
```

### 2. Interceptor de Autenticación

El `authInterceptor` agrega automáticamente el token JWT a todas las peticiones HTTP:

```typescript
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const token = localStorage.getItem('authToken');
  
  if (token) {
    const clonedRequest = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
    return next(clonedRequest);
  }
  
  return next(req);
};
```

### 3. Configuración de API

La configuración de endpoints se encuentra en `shared/constants/api.constants.ts`:

```typescript
export const API_CONFIG = {
  baseUrl: 'http://localhost:8080',
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

## Servicios Disponibles

### 1. AuthService

Gestiona la autenticación de usuarios.

```typescript
import { AuthService } from './services';

constructor(private authService: AuthService) {}

// Login
login() {
  const credentials = { email: 'admin@restaurapp.com', password: 'admin123' };
  this.authService.login(credentials).subscribe(response => {
    console.log('Login exitoso:', response);
    // El token se guarda automáticamente en localStorage
  });
}

// Logout
logout() {
  this.authService.logout();
}

// Verificar si está autenticado
isAuthenticated() {
  return this.authService.isAuthenticated();
}

// Obtener rol del usuario
getUserRole() {
  return this.authService.getUserRole();
}
```

**Endpoints:**
- `POST /auth/login`: Autenticación de usuario

### 2. RoleService

Gestiona los roles del sistema.

```typescript
import { RoleService } from './services';

constructor(private roleService: RoleService) {}

// Obtener todos los roles
getAllRoles() {
  this.roleService.getAll().subscribe(roles => {
    console.log('Roles:', roles);
  });
}

// Crear rol
createRole() {
  const role = { name: 'Manager' };
  this.roleService.create(role).subscribe(newRole => {
    console.log('Rol creado:', newRole);
  });
}

// Actualizar rol
updateRole() {
  const role = { roleId: 1, name: 'Administrator' };
  this.roleService.update(role).subscribe(updatedRole => {
    console.log('Rol actualizado:', updatedRole);
  });
}

// Eliminar rol
deleteRole(roleId: number) {
  this.roleService.delete(roleId).subscribe(() => {
    console.log('Rol eliminado');
  });
}
```

**Endpoints:**
- `GET /role`: Obtener todos los roles
- `POST /role`: Crear un nuevo rol
- `PUT /role`: Actualizar un rol existente
- `DELETE /role`: Eliminar un rol

### 3. StatusService

Gestiona los estados del sistema (para pedidos, mesas, etc.).

```typescript
import { StatusService } from './services';

constructor(private statusService: StatusService) {}

// Métodos disponibles: getAll(), create(), update(), delete()
```

**Endpoints:**
- `GET /status`: Obtener todos los estados
- `POST /status`: Crear un nuevo estado
- `PUT /status`: Actualizar un estado existente
- `DELETE /status`: Eliminar un estado

### 4. UserService

Gestiona los usuarios del sistema.

```typescript
import { UserService } from './services';

constructor(private userService: UserService) {}

// Obtener todos los usuarios
getAllUsers() {
  this.userService.getAll().subscribe(users => {
    console.log('Usuarios:', users);
  });
}

// Crear usuario
createUser() {
  const user = {
    name: 'Juan Pérez',
    email: 'juan.perez@restaurapp.com',
    password: 'password123',
    role: { roleId: 2 }
  };
  this.userService.create(user).subscribe(newUser => {
    console.log('Usuario creado:', newUser);
  });
}

// Eliminar usuario
deleteUser(userId: number) {
  this.userService.delete(userId).subscribe(() => {
    console.log('Usuario eliminado');
  });
}
```

**Endpoints:**
- `GET /user`: Obtener todos los usuarios
- `POST /user`: Crear un nuevo usuario
- `DELETE /user`: Eliminar un usuario

### 5. CategoryService

Gestiona las categorías de productos.

```typescript
import { CategoryService } from './services';

constructor(private categoryService: CategoryService) {}

// Métodos disponibles: getAll(), create(), update(), delete()
```

**Endpoints:**
- `GET /category`: Obtener todas las categorías
- `POST /category`: Crear una nueva categoría
- `PUT /category`: Actualizar una categoría existente
- `DELETE /category`: Eliminar una categoría

### 6. ProductService

Gestiona los productos del restaurante.

```typescript
import { ProductService } from './services';

constructor(private productService: ProductService) {}

// Obtener todos los productos
getAllProducts() {
  this.productService.getAll().subscribe(products => {
    console.log('Productos:', products);
  });
}

// Crear producto
createProduct() {
  const product = {
    name: 'Caesar Salad',
    category: { categoryId: 1 },
    price: 8500,
    stock: 50,
    status: true
  };
  this.productService.create(product).subscribe(newProduct => {
    console.log('Producto creado:', newProduct);
  });
}

// Actualizar producto
updateProduct() {
  const product = {
    productId: 1,
    name: 'Caesar Salad',
    category: { categoryId: 1 },
    price: 9000,
    stock: 50,
    status: true
  };
  this.productService.update(product).subscribe(updatedProduct => {
    console.log('Producto actualizado:', updatedProduct);
  });
}

// Eliminar producto
deleteProduct(productId: number) {
  this.productService.delete(productId).subscribe(() => {
    console.log('Producto eliminado');
  });
}
```

**Endpoints:**
- `GET /product`: Obtener todos los productos
- `POST /product`: Crear un nuevo producto
- `PUT /product`: Actualizar un producto existente
- `DELETE /product`: Eliminar un producto

### 7. TableService

Gestiona las mesas del restaurante.

```typescript
import { TableService } from './services';

constructor(private tableService: TableService) {}

// Crear mesa
createTable() {
  const table = {
    capacity: 4,
    location: 1,
    status: { statusId: 1 } // Available
  };
  this.tableService.create(table).subscribe(newTable => {
    console.log('Mesa creada:', newTable);
  });
}

// Actualizar estado de mesa
updateTableStatus() {
  const table = {
    tableId: 1,
    capacity: 4,
    location: 1,
    status: { statusId: 2 } // Occupied
  };
  this.tableService.update(table).subscribe(updatedTable => {
    console.log('Mesa actualizada:', updatedTable);
  });
}
```

**Endpoints:**
- `GET /table`: Obtener todas las mesas
- `POST /table`: Crear una nueva mesa
- `PUT /table`: Actualizar una mesa existente
- `DELETE /table`: Eliminar una mesa

### 8. OrderTicketService

Gestiona los tickets de pedidos (el servicio más completo).

```typescript
import { OrderTicketService } from './services';

constructor(private orderTicketService: OrderTicketService) {}

// Obtener todos los pedidos
getAllOrderTickets() {
  this.orderTicketService.getAll().subscribe(orders => {
    console.log('Pedidos:', orders);
  });
}

// Crear pedido
createOrderTicket() {
  const order = {
    date: '2025-11-14T14:30:00',
    table: { tableId: 1 },
    waiter: { userId: 2 },
    chef: { userId: 3 },
    status: { statusId: 1 } // Pending
  };
  this.orderTicketService.create(order).subscribe(newOrder => {
    console.log('Pedido creado:', newOrder);
  });
}

// Actualizar estado del pedido
updateOrderStatus(orderId: number, statusId: number) {
  this.orderTicketService.updateStatus(orderId, statusId).subscribe(updatedOrder => {
    console.log('Estado del pedido actualizado:', updatedOrder);
  });
}

// Obtener pedidos por estado
getOrdersByStatus(statusId: number) {
  this.orderTicketService.getByStatus(statusId).subscribe(orders => {
    console.log('Pedidos con estado', statusId, ':', orders);
  });
}

// Obtener pedidos por mesero
getOrdersByWaiter(waiterId: number) {
  this.orderTicketService.getByWaiter(waiterId).subscribe(orders => {
    console.log('Pedidos del mesero:', orders);
  });
}

// Obtener pedidos por chef
getOrdersByChef(chefId: number) {
  this.orderTicketService.getByChef(chefId).subscribe(orders => {
    console.log('Pedidos del chef:', orders);
  });
}

// Obtener items de un pedido
getOrderItems(orderId: number) {
  this.orderTicketService.getOrderItems(orderId).subscribe(items => {
    console.log('Items del pedido:', items);
  });
}
```

**Endpoints:**
- `GET /order-ticket`: Obtener todos los tickets
- `POST /order-ticket`: Crear un nuevo ticket
- `PUT /order-ticket`: Actualizar un ticket (completo)
- `PATCH /order-ticket/{id}/status/{statusId}`: Actualizar solo el estado
- `DELETE /order-ticket`: Eliminar un ticket
- `GET /order-ticket/status/{statusId}`: Obtener tickets por estado
- `GET /order-ticket/waiter/{waiterId}`: Obtener tickets por mesero
- `GET /order-ticket/chef/{chefId}`: Obtener tickets por chef
- `GET /order-ticket/{id}/items`: Obtener items de un ticket

### 9. OrderItemService

Gestiona los items individuales de cada pedido.

```typescript
import { OrderItemService } from './services';

constructor(private orderItemService: OrderItemService) {}

// Crear item de pedido
createOrderItem() {
  const orderItem = {
    orderTicket: { orderTicketId: 1 },
    product: { productId: 1 },
    quantity: 2,
    subtotal: 18000
  };
  this.orderItemService.create(orderItem).subscribe(newItem => {
    console.log('Item de pedido creado:', newItem);
  });
}

// Actualizar item de pedido
updateOrderItem() {
  const orderItem = {
    orderItemId: 1,
    orderTicket: { orderTicketId: 1 },
    product: { productId: 1 },
    quantity: 3,
    subtotal: 27000
  };
  this.orderItemService.update(orderItem).subscribe(updatedItem => {
    console.log('Item de pedido actualizado:', updatedItem);
  });
}
```

**Endpoints:**
- `GET /order-item`: Obtener todos los items
- `POST /order-item`: Crear un nuevo item
- `PUT /order-item`: Actualizar un item existente
- `DELETE /order-item`: Eliminar un item

## Modelos de Datos

### Role
```typescript
interface Role {
  roleId?: number;
  name: string;
}
```

### Status
```typescript
interface Status {
  statusId?: number;
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

### Category
```typescript
interface Category {
  categoryId?: number;
  name: string;
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

### Table
```typescript
interface Table {
  tableId?: number;
  capacity: number;
  location: number;
  status: Status;
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

### OrderItem
```typescript
interface OrderItem {
  orderItemId?: number;
  orderTicket: OrderTicket;
  product: Product;
  quantity: number;
  subtotal: number;
}
```

## Manejo de Errores

Todos los servicios retornan Observables. Puedes manejar errores usando el operador `catchError` de RxJS:

```typescript
import { catchError } from 'rxjs/operators';
import { of } from 'rxjs';

this.productService.getAll().pipe(
  catchError(error => {
    console.error('Error al obtener productos:', error);
    return of([]); // Retorna un array vacío en caso de error
  })
).subscribe(products => {
  console.log('Productos:', products);
});
```

## Ejemplo Completo de Componente

```typescript
import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { 
  ProductService, 
  CategoryService, 
  AuthService 
} from '../services';
import { Product } from '../shared/models';

@Component({
  selector: 'app-products',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="products-container">
      <h2>Productos</h2>
      <div *ngFor="let product of products" class="product-card">
        <h3>{{ product.name }}</h3>
        <p>Categoría: {{ product.category.name }}</p>
        <p>Precio: ${{ product.price }}</p>
        <p>Stock: {{ product.stock }}</p>
        <button (click)="deleteProduct(product.productId!)">Eliminar</button>
      </div>
    </div>
  `
})
export class ProductsComponent implements OnInit {
  private productService = inject(ProductService);
  private authService = inject(AuthService);
  
  products: Product[] = [];

  ngOnInit() {
    if (this.authService.isAuthenticated()) {
      this.loadProducts();
    }
  }

  loadProducts() {
    this.productService.getAll().subscribe({
      next: (products) => {
        this.products = products;
      },
      error: (error) => {
        console.error('Error al cargar productos:', error);
      }
    });
  }

  deleteProduct(productId: number) {
    this.productService.delete(productId).subscribe({
      next: () => {
        console.log('Producto eliminado');
        this.loadProducts(); // Recargar la lista
      },
      error: (error) => {
        console.error('Error al eliminar producto:', error);
      }
    });
  }
}
```

## Instalación y Ejecución

### Prerrequisitos
- Node.js >= 18
- Angular CLI >= 20

### Instalación
```bash
cd resource
npm install
```

### Desarrollo
```bash
npm start
```

La aplicación estará disponible en `http://localhost:4200`

### Build de Producción
```bash
npm run build
```

## Notas Importantes

1. **Autenticación**: Todos los servicios (excepto AuthService.login) requieren autenticación. El token se agrega automáticamente mediante el interceptor.

2. **CORS**: Asegúrate de que el backend esté configurado para aceptar peticiones desde `http://localhost:4200`.

3. **Configuración de API**: Si el backend está en una URL diferente a `http://localhost:8080`, actualiza `API_CONFIG` en `shared/constants/api.constants.ts`.

4. **Inyección de Dependencias**: Usa el nuevo sistema de `inject()` de Angular para servicios standalone.

5. **Modelos Opcionales**: Los IDs en los modelos son opcionales (con `?`) porque no se envían al crear nuevos registros.

## Próximos Pasos

1. Crear guards para proteger rutas según roles
2. Implementar componentes de UI para cada entidad
3. Agregar validación de formularios
4. Implementar paginación para listas grandes
5. Agregar filtros y búsqueda
6. Implementar caché para datos estáticos (roles, categorías, estados)
7. Agregar loading states y spinners
8. Implementar notificaciones/toasts para feedback de usuario

## Soporte

Para más información sobre el backend, consulta la colección de Postman incluida en el proyecto: `RestaurAppv1.postman_collection.json`

