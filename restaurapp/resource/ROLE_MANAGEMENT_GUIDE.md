# 👥 Guía de Gestión de Roles

## 📋 Descripción

Este documento explica cómo utilizar el módulo de **Gestión de Roles** de RestaurApp, una interfaz completa para administrar roles del sistema mediante operaciones CRUD (Create, Read, Update, Delete).

---

## 🚀 Acceso al Módulo

### Requisitos:
- ✅ Estar autenticado
- ✅ Tener rol de **Admin**

### URL de Acceso:
```
http://localhost:4200/admin/roles
```

### Desde el Dashboard:
1. Inicia sesión como **Admin**
2. En el Dashboard verás el **Panel de Administración**
3. Click en la tarjeta **"Gestión de Roles"**

---

## ✨ Características

### 📊 Vista Principal
- **Tabla de Roles**: Lista todos los roles existentes con:
  - ID del rol
  - Nombre del rol
  - Badges de color según el tipo de rol
  - Acciones (Editar/Eliminar)
  
- **Contador de Roles**: Muestra el número total de roles
- **Botón Nuevo Rol**: Permite crear un nuevo rol

### ➕ Crear Nuevo Rol
1. Click en **"Nuevo Rol"**
2. Se abre un modal con el formulario
3. Ingresa el nombre del rol (máximo 20 caracteres)
4. Click en **"Crear"**
5. Notificación de éxito

### ✏️ Editar Rol
1. Click en **"Editar"** en la fila del rol
2. Se abre el modal con los datos pre-cargados
3. Modifica el nombre del rol
4. Click en **"Actualizar"**
5. Notificación de éxito

### 🗑️ Eliminar Rol
1. Click en **"Eliminar"** en la fila del rol
2. Se muestra un diálogo de confirmación
3. Confirma la eliminación
4. Notificación de éxito

**⚠️ Nota:** No se puede eliminar un rol que está siendo usado por usuarios activos.

---

## 🎨 Componentes del Módulo

### Archivos Creados:

```
src/app/features/admin/role-management/
├── role-management.component.ts      # Lógica del componente
├── role-management.component.html    # Template HTML
└── role-management.component.scss    # Estilos
```

### Estructura del Componente:

```typescript
@Component({
  selector: 'app-role-management',
  standalone: true,
  imports: [CommonModule, FormsModule, LoadingComponent],
  templateUrl: './role-management.component.html',
  styleUrl: './role-management.component.scss'
})
export class RoleManagementComponent implements OnInit
```

---

## 🔧 Funcionalidades Implementadas

### 1. Listado de Roles
```typescript
loadRoles(): void {
  this.roleService.getAll().subscribe({
    next: (roles) => {
      this.roles.set(roles);
      this.isLoading.set(false);
    },
    error: (error) => {
      this.notificationService.error('Error al cargar los roles');
      this.isLoading.set(false);
    }
  });
}
```

### 2. Crear Rol
```typescript
openCreateModal(): void {
  this.currentRole.set({ name: '' });
  this.isEditing.set(false);
  this.showModal.set(true);
}
```

### 3. Actualizar Rol
```typescript
openEditModal(role: Role): void {
  this.currentRole.set({ ...role });
  this.isEditing.set(true);
  this.showModal.set(true);
}
```

### 4. Eliminar Rol
```typescript
deleteRole(role: Role): void {
  if (!confirm(`¿Está seguro de eliminar el rol "${role.name}"?`)) {
    return;
  }

  this.roleService.delete(role.roleId).subscribe({
    next: () => {
      this.notificationService.success(`Rol "${role.name}" eliminado`);
      this.loadRoles();
    },
    error: (error) => {
      this.notificationService.error('Error al eliminar el rol');
    }
  });
}
```

---

## 🎨 Diseño y UX

### Paleta de Colores por Rol:
- **Admin**: Gradiente Rosa-Rojo (#f093fb → #f5576c)
- **Waiter**: Gradiente Azul (#4facfe → #00f2fe)
- **Chef**: Gradiente Verde (#43e97b → #38f9d7)
- **Otros**: Gradiente Púrpura (#667eea → #764ba2)

### Estados Visuales:
- ⏳ **Loading**: Spinner mientras carga datos
- ✅ **Success**: Notificaciones verdes
- ❌ **Error**: Notificaciones rojas
- ⚠️ **Warning**: Notificaciones amarillas
- 📋 **Empty State**: Mensaje cuando no hay roles

### Animaciones:
- Modal: `slideUp` + `fadeIn`
- Hover en tarjetas: `translateY(-5px)` con shadow
- Transiciones suaves en todos los elementos

---

## 🔒 Seguridad

### Protección de Rutas:
```typescript
{
  path: 'admin',
  canActivate: [authGuard, roleGuard],
  data: { roles: ['Admin'] },
  children: [
    {
      path: 'roles',
      loadComponent: () => import('./features/admin/role-management/...')
    }
  ]
}
```

### Guards Aplicados:
1. **authGuard**: Verifica autenticación
2. **roleGuard**: Verifica rol de Admin

---

## 📱 Responsive Design

### Breakpoints:
- **Desktop**: > 768px (Grid de 3 columnas)
- **Tablet**: 768px (Grid ajustado)
- **Mobile**: < 768px (1 columna, botones full-width)

### Ajustes Móviles:
```scss
@media (max-width: 768px) {
  .role-management-container {
    padding: 20px;
  }
  
  .page-header {
    flex-direction: column;
    .btn-primary {
      width: 100%;
      justify-content: center;
    }
  }
}
```

---

## 🧪 Pruebas

### Flujo de Prueba Completo:

1. **Login como Admin**
   ```
   Email: admin@restaurapp.com
   Password: admin123
   ```

2. **Navegar a Gestión de Roles**
   - Desde dashboard: Click en tarjeta "Gestión de Roles"
   - O directamente: `http://localhost:4200/admin/roles`

3. **Crear un Nuevo Rol**
   - Click en "Nuevo Rol"
   - Nombre: "Manager"
   - Click en "Crear"
   - ✅ Verificar notificación de éxito
   - ✅ Verificar que aparece en la tabla

4. **Editar el Rol**
   - Click en "Editar" del rol "Manager"
   - Cambiar nombre a "Manager Premium"
   - Click en "Actualizar"
   - ✅ Verificar notificación de éxito
   - ✅ Verificar cambio en la tabla

5. **Eliminar el Rol**
   - Click en "Eliminar" del rol "Manager Premium"
   - Confirmar eliminación
   - ✅ Verificar notificación de éxito
   - ✅ Verificar que desaparece de la tabla

---

## 🚨 Manejo de Errores

### Errores Comunes:

1. **Error 401**: Usuario no autenticado
   - **Solución**: Redirigir a login

2. **Error 403**: Usuario sin permisos
   - **Solución**: Mostrar "Acceso denegado"

3. **Error 409**: Rol en uso (al eliminar)
   - **Mensaje**: "No se puede eliminar el rol porque está siendo usado"

4. **Error 500**: Error del servidor
   - **Mensaje**: "Error del servidor. Intente nuevamente"

### Validaciones del Formulario:

```html
<!-- Nombre requerido -->
<div *ngIf="roleNameInput.touched && roleNameInput.errors?.['required']">
  El nombre del rol es requerido
</div>

<!-- Máximo 20 caracteres -->
<input maxlength="20" />

<!-- Contador de caracteres -->
<div class="form-hint">
  {{ currentRole().name.length }}/20 caracteres
</div>
```

---

## 🔄 Integración con Backend

### Endpoints Utilizados:

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/role` | Obtener todos los roles |
| POST | `/role` | Crear nuevo rol |
| PUT | `/role` | Actualizar rol |
| DELETE | `/role/{id}` | Eliminar rol |

### Servicio Utilizado:
```typescript
@Injectable({ providedIn: 'root' })
export class RoleService {
  getAll(): Observable<Role[]>
  getById(id: number): Observable<Role>
  create(role: Role): Observable<Role>
  update(role: Role): Observable<Role>
  delete(id: number): Observable<void>
}
```

---

## 💡 Próximas Mejoras

### Funcionalidades Futuras:
- [ ] Búsqueda y filtrado de roles
- [ ] Ordenamiento por columnas
- [ ] Paginación para grandes volúmenes
- [ ] Exportar roles a CSV/Excel
- [ ] Asignación de permisos por rol
- [ ] Historial de cambios
- [ ] Roles predeterminados no eliminables

---

## 📚 Recursos Adicionales

### Documentación Relacionada:
- `README.md` - Guía general del proyecto
- `SERVICES.md` - Documentación de servicios
- `EXAMPLES.md` - Ejemplos de componentes
- `PROJECT_SUMMARY.md` - Resumen ejecutivo

### Servicios Relacionados:
- `RoleService` - Operaciones CRUD de roles
- `NotificationService` - Sistema de notificaciones
- `AuthService` - Autenticación y autorización

### Guards:
- `authGuard` - Protección de autenticación
- `roleGuard` - Protección por rol

---

## 🎯 Resumen

✅ **Implementado:**
- Tabla de roles con acciones
- Modal para crear/editar roles
- Confirmación para eliminar roles
- Notificaciones de éxito/error
- Validación de formularios
- Diseño responsive
- Protección por roles (Admin only)
- Manejo de errores
- Loading states
- Empty states

🚀 **Resultado:**
Un módulo completo y profesional para la gestión de roles con excelente UX/UI.

---

**Fecha de Creación:** 14 de Noviembre, 2025  
**Versión Angular:** 20.0.0  
**Autor:** RestaurApp Team



