import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UserService, RoleService, NotificationService } from '../../../services';
import { User, Role } from '../../../shared/models';
import { LoadingComponent } from '../../../shared/components';

@Component({
  selector: 'app-user-management',
  standalone: true,
  imports: [CommonModule, FormsModule, LoadingComponent],
  templateUrl: './user-management.component.html',
  styleUrl: './user-management.component.scss'
})
export class UserManagementComponent implements OnInit {
  private userService = inject(UserService);
  private roleService = inject(RoleService);
  private notificationService = inject(NotificationService);

  users = signal<User[]>([]);
  roles = signal<Role[]>([]);
  isLoading = signal(false);
  showModal = signal(false);
  isEditing = signal(false);
  showPassword = signal(false);
  
  currentUser = signal<User>({
    name: '',
    email: '',
    password: '',
    role: { roleId: undefined, name: '' }
  });

  ngOnInit(): void {
    this.loadUsers();
    this.loadRoles();
  }

  /**
   * Carga todos los usuarios desde el backend
   */
  loadUsers(): void {
    this.isLoading.set(true);
    
    this.userService.getAll().subscribe({
      next: (users) => {
        this.users.set(users);
        this.isLoading.set(false);
      },
      error: (error) => {
        console.error('Error al cargar usuarios:', error);
        this.notificationService.error('Error al cargar los usuarios');
        this.isLoading.set(false);
      }
    });
  }

  /**
   * Carga todos los roles disponibles
   */
  loadRoles(): void {
    this.roleService.getAll().subscribe({
      next: (roles) => {
        this.roles.set(roles);
      },
      error: (error) => {
        console.error('Error al cargar roles:', error);
        this.notificationService.error('Error al cargar los roles');
      }
    });
  }

  /**
   * Abre el modal para crear un nuevo usuario
   */
  openCreateModal(): void {
    this.currentUser.set({
      name: '',
      email: '',
      password: '',
      role: { roleId: undefined, name: '' }
    });
    this.isEditing.set(false);
    this.showPassword.set(false);
    this.showModal.set(true);
  }

  /**
   * Abre el modal para editar un usuario existente
   */
  openEditModal(user: User): void {
    this.currentUser.set({
      ...user,
      password: '', // No mostrar password en edición
      role: { ...user.role }
    });
    this.isEditing.set(true);
    this.showPassword.set(false);
    this.showModal.set(true);
  }

  /**
   * Cierra el modal y resetea el formulario
   */
  closeModal(): void {
    this.showModal.set(false);
    this.currentUser.set({
      name: '',
      email: '',
      password: '',
      role: { roleId: undefined, name: '' }
    });
    this.isEditing.set(false);
    this.showPassword.set(false);
  }

  /**
   * Guarda un usuario (crear o actualizar)
   */
  saveUser(): void {
    const user = this.currentUser();
    
    // Validaciones
    if (!user.name.trim()) {
      this.notificationService.warning('El nombre es requerido');
      return;
    }

    if (!user.email.trim()) {
      this.notificationService.warning('El email es requerido');
      return;
    }

    if (!this.isValidEmail(user.email)) {
      this.notificationService.warning('Email no válido');
      return;
    }

    if (!this.isEditing() && !user.password) {
      this.notificationService.warning('La contraseña es requerida');
      return;
    }

    if (user.password && user.password.length < 6) {
      this.notificationService.warning('La contraseña debe tener al menos 6 caracteres');
      return;
    }

    if (!user.role.roleId) {
      this.notificationService.warning('Debe seleccionar un rol');
      return;
    }

    this.isLoading.set(true);

    // Preparar usuario para enviar
    const userToSave: User = {
      ...user,
      role: { 
        roleId: user.role.roleId,
        name: user.role.name || ''
      }
    };

    // Si es edición y no hay password, no enviar el campo
    if (this.isEditing() && !userToSave.password) {
      delete userToSave.password;
    }

    const operation = this.isEditing()
      ? this.userService.update(userToSave)
      : this.userService.create(userToSave);

    operation.subscribe({
      next: (savedUser: User) => {
        const action = this.isEditing() ? 'actualizado' : 'creado';
        this.notificationService.success(`Usuario "${savedUser.name}" ${action} exitosamente`);
        this.closeModal();
        this.loadUsers();
      },
      error: (error: any) => {
        console.error('Error al guardar usuario:', error);
        
        if (error.status === 409) {
          this.notificationService.error('El email ya está en uso');
        } else {
          const action = this.isEditing() ? 'actualizar' : 'crear';
          this.notificationService.error(`Error al ${action} el usuario`);
        }
        
        this.isLoading.set(false);
      }
    });
  }

  /**
   * Elimina un usuario
   */
  deleteUser(user: User): void {
    if (!user.userId) {
      this.notificationService.error('ID de usuario no válido');
      return;
    }

    // Prevenir eliminación del usuario actual
    const currentUserId = localStorage.getItem('userId');
    if (user.userId.toString() === currentUserId) {
      this.notificationService.warning('No puedes eliminar tu propio usuario');
      return;
    }

    const confirmMessage = `¿Está seguro de eliminar al usuario "${user.name}"?\n\nEmail: ${user.email}\nRol: ${user.role.name}\n\nEsta acción no se puede deshacer.`;
    
    if (!confirm(confirmMessage)) {
      return;
    }

    this.isLoading.set(true);

    this.userService.delete(user.userId).subscribe({
      next: () => {
        this.notificationService.success(`Usuario "${user.name}" eliminado exitosamente`);
        this.loadUsers();
      },
      error: (error) => {
        console.error('Error al eliminar usuario:', error);
        this.notificationService.error('Error al eliminar el usuario');
        this.isLoading.set(false);
      }
    });
  }

  /**
   * Actualiza el nombre del usuario en el formulario
   */
  updateUserName(name: string): void {
    this.currentUser.update(user => ({ ...user, name }));
  }

  /**
   * Actualiza el email del usuario en el formulario
   */
  updateUserEmail(email: string): void {
    this.currentUser.update(user => ({ ...user, email }));
  }

  /**
   * Actualiza la contraseña del usuario en el formulario
   */
  updateUserPassword(password: string): void {
    this.currentUser.update(user => ({ ...user, password }));
  }

  /**
   * Actualiza el rol del usuario en el formulario
   */
  updateUserRole(roleId: string): void {
    const selectedRole = this.roles().find(r => r.roleId?.toString() === roleId);
    if (selectedRole) {
      this.currentUser.update(user => ({ 
        ...user, 
        role: { roleId: selectedRole.roleId, name: selectedRole.name } 
      }));
    }
  }

  /**
   * Toggle visibilidad de contraseña
   */
  togglePasswordVisibility(): void {
    this.showPassword.update(show => !show);
  }

  /**
   * Valida formato de email
   */
  isValidEmail(email: string): boolean {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
  }

  /**
   * TrackBy function para optimizar el renderizado de la tabla
   */
  trackByUserId(index: number, user: User): number | undefined {
    return user.userId;
  }

  /**
   * Obtiene la clase CSS según el rol
   */
  getRoleClass(roleName: string): string {
    const role = roleName.toLowerCase();
    if (role === 'admin') return 'role-admin';
    if (role === 'waiter' || role === 'mesero') return 'role-waiter';
    if (role === 'chef') return 'role-chef';
    return 'role-default';
  }

  /**
   * Obtiene el ícono según el rol
   */
  getRoleIcon(roleName: string): string {
    const role = roleName.toLowerCase();
    if (role === 'admin') return '👑';
    if (role === 'waiter' || role === 'mesero') return '🍽️';
    if (role === 'chef') return '👨‍🍳';
    return '👤';
  }
}

