# 👥 Guía de Gestión de Usuarios

## 📋 Descripción

Módulo completo para gestionar el CRUD de **Usuarios del Sistema** en RestaurApp. Permite crear, editar y eliminar usuarios con asignación de roles, validación de email y gestión segura de contraseñas.

---

## 🚀 Acceso al Módulo

### Requisitos:
- ✅ Estar autenticado
- ✅ Tener rol de **Admin**

### URL de Acceso:
```
http://localhost:4200/admin/users
```

### Desde el Dashboard:
1. Login como **Admin**
2. Panel de Administración → **"👤 Gestión de Usuarios"**

---

## ✨ Características Implementadas

### 📊 Vista Principal
- **Tabla de Usuarios** con:
  - ID del usuario
  - Nombre completo con ícono 👤
  - Email con ícono 📧
  - Rol con badge de color
  - Acciones (Editar/Eliminar)
  
- **Badges de Roles con Colores:**
  - 👑 **Admin** → Gradiente Rosa-Rojo
  - 🍽️ **Waiter** → Gradiente Azul
  - 👨‍🍳 **Chef** → Gradiente Verde

### ➕ Crear Nuevo Usuario

**Formulario completo con validaciones:**

1. **Nombre Completo** (requerido)
   - Máximo 50 caracteres
   - Contador de caracteres en tiempo real

2. **Email** (requerido)
   - Validación de formato email
   - Máximo 100 caracteres
   - Detección de emails duplicados

3. **Contraseña** (requerida)
   - Mínimo 6 caracteres
   - Toggle para mostrar/ocultar
   - Máximo 225 caracteres

4. **Rol** (requerido)
   - Selector dropdown
   - Carga dinámica de roles disponibles
   - Info visual de roles con badges

**Proceso:**
1. Click en **"+ Nuevo Usuario"**
2. Completar formulario
3. Seleccionar rol del dropdown
4. Click en **"➕ Crear"**
5. ✅ Notificación de éxito

### ✏️ Editar Usuario

**Características:**
- Modal pre-cargado con datos del usuario
- **Contraseña opcional**: Dejar vacía para no cambiarla
- Todos los campos editables excepto ID
- Validación en tiempo real

**Proceso:**
1. Click en **"✏️ Editar"** en la tabla
2. Modificar campos deseados
3. Password opcional (vacío = sin cambios)
4. Click en **"💾 Actualizar"**
5. ✅ Notificación de éxito

### 🗑️ Eliminar Usuario

**Características de seguridad:**
- ⚠️ **Protección**: No puedes eliminar tu propio usuario
- Confirmación detallada con:
  - Nombre del usuario
  - Email
  - Rol
- Advertencia de acción irreversible

**Proceso:**
1. Click en **"🗑️ Eliminar"**
2. Revisar confirmación
3. Confirmar eliminación
4. ✅ Usuario eliminado

---

## 🔒 Seguridad Implementada

### **1. Protección de Ruta**
```typescript
{
  path: 'admin/users',
  canActivate: [authGuard, roleGuard],
  data: { roles: ['Admin'] }
}
```

### **2. Validación de Email**
```typescript
isValidEmail(email: string): boolean {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(email);
}
```

### **3. Auto-protección**
```typescript
// Prevenir eliminación del usuario actual
const currentUserId = localStorage.getItem('userId');
if (user.userId.toString() === currentUserId) {
  this.notificationService.warning('No puedes eliminar tu propio usuario');
  return;
}
```

### **4. Gestión de Contraseñas**
- Toggle de visibilidad 👁️ / 👁️‍🗨️
- Mínimo 6 caracteres
- En edición: opcional (vacío = sin cambios)
- No se muestra la contraseña actual

---

## 🎨 Diseño y UX

### **Paleta de Colores por Rol:**
```scss
Admin:  Gradiente Rosa-Rojo  (#f093fb → #f5576c)
Waiter: Gradiente Azul       (#4facfe → #00f2fe)
Chef:   Gradiente Verde      (#43e97b → #38f9d7)
Default: Gradiente Púrpura   (#667eea → #764ba2)
```

### **Estados Visuales:**
- ⏳ **Loading**: Spinner durante operaciones
- ✅ **Success**: Notificaciones verdes
- ❌ **Error**: Notificaciones rojas
- ⚠️ **Warning**: Notificaciones amarillas
- 📋 **Empty State**: Sin usuarios registrados

### **Animaciones:**
- Modal: `slideUp` + `fadeIn`
- Hover en filas de tabla
- Transiciones suaves en botones
- Toggle de contraseña animado

---

## 📱 Responsive Design

| Dispositivo | Tabla | Formulario | Botones |
|-------------|-------|------------|---------|
| **Desktop (>768px)** | Completa | 2 columnas | Normal |
| **Tablet (768px)** | Scroll H | 2 columnas | Ajustado |
| **Mobile (<768px)** | Scroll H | 1 columna | Full width |

---

## 🧪 Casos de Uso Prácticos

### **Caso 1: Crear Usuario Administrador**
```
Nombre: Carlos Rodríguez
Email: carlos@restaurapp.com
Password: admin123
Rol: Admin 👑
```

### **Caso 2: Crear Usuario Mesero**
```
Nombre: Ana García
Email: ana.garcia@restaurapp.com
Password: mesero123
Rol: Waiter 🍽️
```

### **Caso 3: Actualizar Rol de Usuario**
1. Abrir edición de usuario "Ana García"
2. Cambiar rol de "Waiter" a "Chef"
3. Dejar contraseña vacía
4. Guardar cambios
5. ✅ Rol actualizado sin cambiar contraseña

### **Caso 4: Cambiar Contraseña**
1. Editar usuario
2. Completar todos los campos
3. Ingresar nueva contraseña
4. Toggle para verificar
5. Guardar
6. ✅ Contraseña actualizada

---

## 🔧 Funcionalidades Técnicas

### **1. Carga de Usuarios y Roles**
```typescript
ngOnInit(): void {
  this.loadUsers();    // Carga todos los usuarios
  this.loadRoles();    // Carga roles para el selector
}
```

### **2. Validaciones del Formulario**
```typescript
// Validaciones implementadas:
✅ Nombre requerido (máx 50 chars)
✅ Email requerido y válido (máx 100 chars)
✅ Password requerido en creación (mín 6 chars)
✅ Password opcional en edición
✅ Rol requerido (selección de dropdown)
✅ Email único (detecta duplicados)
```

### **3. Preparación de Datos**
```typescript
const userToSave: User = {
  ...user,
  role: { 
    roleId: user.role.roleId,
    name: user.role.name || ''
  }
};

// En edición: no enviar password si está vacío
if (this.isEditing() && !userToSave.password) {
  delete userToSave.password;
}
```

### **4. Manejo de Errores**
```typescript
✅ Error 409: Email duplicado
✅ Error 500: Error del servidor
✅ Validación de campos vacíos
✅ Protección contra auto-eliminación
```

---

## 📋 Validaciones del Formulario

### **Campo: Nombre**
```html
<input 
  type="text"
  required
  maxlength="50"
  placeholder="Ej: Juan Pérez">

<!-- Errores -->
⚠️ "El nombre completo es requerido"
💡 Contador: "25/50 caracteres"
```

### **Campo: Email**
```html
<input 
  type="email"
  required
  email
  maxlength="100"
  placeholder="ejemplo@restaurapp.com">

<!-- Errores -->
⚠️ "El email es requerido"
⚠️ "Email no válido"
⚠️ "El email ya está en uso" (409)
```

### **Campo: Contraseña**
```html
<input 
  type="password"
  required (solo en crear)
  minlength="6"
  maxlength="225"
  placeholder="Mínimo 6 caracteres">

<!-- Errores -->
⚠️ "La contraseña es requerida" (crear)
⚠️ "La contraseña debe tener al menos 6 caracteres"
💡 Toggle: Mostrar/Ocultar
💡 Tip: "Usa una contraseña segura"
```

### **Campo: Rol**
```html
<select required>
  <option value="" disabled>Selecciona un rol</option>
  <option *ngFor="let role of roles()" [value]="role.roleId">
    {{ role.name }}
  </option>
</select>

<!-- Errores -->
⚠️ "Debe seleccionar un rol"

<!-- Info Visual -->
👑 Admin   🍽️ Waiter   👨‍🍳 Chef
```

---

## 🔄 Integración con Backend

### **Endpoints Utilizados:**

| Método | Endpoint | Descripción | Body |
|--------|----------|-------------|------|
| GET | `/user` | Obtener todos los usuarios | - |
| POST | `/user` | Crear nuevo usuario | User object |
| PUT | `/user` | Actualizar usuario | User object |
| DELETE | `/user` | Eliminar usuario | { userId } |
| GET | `/role` | Obtener roles disponibles | - |

### **Modelo de Usuario:**
```typescript
interface User {
  userId?: number;
  name: string;          // Máx 50 chars
  email: string;         // Máx 100 chars, único
  password?: string;     // Mín 6 chars, max 225
  role: Role;            // Relación con Role
}

interface Role {
  roleId?: number;
  name: string;
}
```

### **Servicios Utilizados:**
```typescript
UserService:
  - getAll(): Observable<User[]>
  - create(user): Observable<User>
  - update(user): Observable<User>
  - delete(userId): Observable<void>

RoleService:
  - getAll(): Observable<Role[]>

NotificationService:
  - success(message)
  - error(message)
  - warning(message)
```

---

## 🧪 Prueba Completa Paso a Paso

### **Paso 1: Acceso**
```bash
1. Login: admin@restaurapp.com / admin123
2. Dashboard → "Gestión de Usuarios"
3. URL: http://localhost:4200/admin/users
```

### **Paso 2: Crear Usuario Mesero**
```
1. Click "+" Nuevo Usuario"
2. Nombre: "Pedro López"
3. Email: "pedro@restaurapp.com"
4. Password: "mesero123" (toggle para ver)
5. Rol: Seleccionar "Waiter" 🍽️
6. Click "➕ Crear"
7. ✅ Notificación: "Usuario 'Pedro López' creado exitosamente"
8. ✅ Aparece en tabla con badge azul de Waiter
```

### **Paso 3: Editar Usuario**
```
1. Click "✏️ Editar" en "Pedro López"
2. Cambiar nombre a "Pedro López García"
3. Cambiar rol a "Chef" 👨‍🍳
4. Dejar password vacío (no cambiar)
5. Click "💾 Actualizar"
6. ✅ Notificación: "Usuario actualizado exitosamente"
7. ✅ Badge cambia a verde de Chef
```

### **Paso 4: Intentar Eliminar Usuario Actual**
```
1. Click "🗑️ Eliminar" en tu propio usuario (Admin)
2. ⚠️ Notificación: "No puedes eliminar tu propio usuario"
3. ✅ Operación bloqueada por seguridad
```

### **Paso 5: Eliminar Usuario**
```
1. Click "🗑️ Eliminar" en "Pedro López García"
2. Ventana de confirmación:
   - Nombre: Pedro López García
   - Email: pedro@restaurapp.com
   - Rol: Chef
   - "Esta acción no se puede deshacer"
3. Click "Aceptar"
4. ✅ Notificación: "Usuario 'Pedro López García' eliminado"
5. ✅ Desaparece de la tabla
```

---

## 💡 Tips y Mejores Prácticas

### ✅ **Recomendaciones:**
- Usa emails corporativos únicos
- Contraseñas seguras de al menos 8 caracteres
- Asigna roles según responsabilidades reales
- Revisa la tabla antes de eliminar usuarios
- Usa la función de edición para cambiar roles

### ⚠️ **Errores Comunes:**
- **Email duplicado**: Cada usuario debe tener email único
- **Password muy corta**: Mínimo 6 caracteres
- **Sin rol**: Todos los usuarios necesitan un rol asignado
- **Auto-eliminación**: No puedes eliminar tu propio usuario

### 🚀 **Optimizaciones:**
- **TrackBy**: Optimiza renderizado de tabla
- **Lazy Loading**: Carga bajo demanda
- **Validación en tiempo real**: Feedback inmediato
- **Toggle de contraseña**: Mejor UX

---

## 📁 Estructura de Archivos

```
src/app/features/admin/user-management/
├── user-management.component.ts      ✅ 290 líneas
├── user-management.component.html    ✅ 237 líneas
└── user-management.component.scss    ✅ 277 líneas (7.12KB)

src/app/services/
└── user.service.ts                   ✅ Agregado método update()
```

---

## 📊 Resumen Técnico

```typescript
✅ Angular 20 Standalone Components
✅ Signals para estado reactivo
✅ Lazy loading de rutas
✅ Guards de seguridad (auth + role)
✅ UserService con CRUD completo
✅ RoleService para selector dinámico
✅ NotificationService para feedback
✅ LoadingComponent para UX
✅ Forms con validación completa
✅ TrackBy para performance
✅ Responsive design
✅ Toggle de contraseña
✅ Validación de email
✅ Protección contra auto-eliminación
✅ Detección de emails duplicados
✅ Password opcional en edición
✅ Badges de roles con colores
✅ Animaciones CSS
✅ Manejo completo de errores
✅ Empty states
```

---

## 🎉 Estado del Proyecto

### **Compilación:**
```bash
✅ Application bundle generation complete
📦 Bundle size: 21.87 kB (user-management)
⚠️ Warnings CSS budget (aceptables, < 10KB)
```

### **Funcionalidades:**
- ✅ Listar todos los usuarios con roles
- ✅ Crear nuevo usuario con validaciones
- ✅ Editar usuario existente
- ✅ Actualizar rol de usuario
- ✅ Cambiar contraseña (opcional en edición)
- ✅ Eliminar usuario con confirmación
- ✅ Protección contra auto-eliminación
- ✅ Validación de email único
- ✅ Selector de roles dinámico
- ✅ Toggle de contraseña
- ✅ Notificaciones de éxito/error
- ✅ Loading states
- ✅ Empty states
- ✅ Responsive design

---

## 🎯 Módulos Admin Completados

```
✅ Roles Management    → Completoy funcional
✅ Status Management   → Completo y funcional
✅ Users Management    → Completo y funcional (NUEVO)
🔜 Products Management → Próximo
🔜 Categories Management → Próximo
🔜 Tables Management   → Próximo
```

---

## 📚 Recursos Relacionados

### **Documentación:**
- `README.md` - Guía general del proyecto
- `ROLE_MANAGEMENT_GUIDE.md` - Guía de roles
- `STATUS_MANAGEMENT_GUIDE.md` - Guía de estados
- `SERVICES.md` - Documentación de servicios

### **Servicios:**
- `UserService` - CRUD de usuarios
- `RoleService` - Obtener roles
- `NotificationService` - Sistema de notificaciones
- `AuthService` - Autenticación

### **Guards:**
- `authGuard` - Protección de autenticación
- `roleGuard` - Protección por rol

---

**Fecha:** 14 de Noviembre, 2025  
**Versión Angular:** 20.0.0  
**Status:** ✅ Completado y Funcional

---

¡Tu módulo de gestión de usuarios está listo y seguro para producción! 🚀



