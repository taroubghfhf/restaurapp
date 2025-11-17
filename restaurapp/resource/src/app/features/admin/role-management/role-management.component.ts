import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RoleService, NotificationService } from '../../../services';
import { Role } from '../../../shared/models';
import { LoadingComponent } from '../../../shared/components';

@Component({
  selector: 'app-role-management',
  standalone: true,
  imports: [CommonModule, FormsModule, LoadingComponent],
  templateUrl: './role-management.component.html',
  styleUrl: './role-management.component.scss'
})
export class RoleManagementComponent implements OnInit {
  private roleService = inject(RoleService);
  private notificationService = inject(NotificationService);

  roles = signal<Role[]>([]);
  isLoading = signal(false);
  showModal = signal(false);
  isEditing = signal(false);
  
  currentRole = signal<Role>({
    name: ''
  });

  ngOnInit(): void {
    this.loadRoles();
  }

  /**
   * Carga todos los roles desde el backend
   */
  loadRoles(): void {
    this.isLoading.set(true);
    
    this.roleService.getAll().subscribe({
      next: (roles) => {
        this.roles.set(roles);
        this.isLoading.set(false);
      },
      error: (error) => {
        console.error('Error al cargar roles:', error);
        this.notificationService.error('Error al cargar los roles');
        this.isLoading.set(false);
      }
    });
  }

  /**
   * Abre el modal para crear un nuevo rol
   */
  openCreateModal(): void {
    this.currentRole.set({ name: '' });
    this.isEditing.set(false);
    this.showModal.set(true);
  }

  /**
   * Abre el modal para editar un rol existente
   */
  openEditModal(role: Role): void {
    this.currentRole.set({ ...role });
    this.isEditing.set(true);
    this.showModal.set(true);
  }

  /**
   * Cierra el modal y resetea el formulario
   */
  closeModal(): void {
    this.showModal.set(false);
    this.currentRole.set({ name: '' });
    this.isEditing.set(false);
  }

  /**
   * Guarda un rol (crear o actualizar)
   */
  saveRole(): void {
    const role = this.currentRole();
    
    if (!role.name.trim()) {
      this.notificationService.warning('El nombre del rol es requerido');
      return;
    }

    this.isLoading.set(true);

    const operation = this.isEditing()
      ? this.roleService.update(role)
      : this.roleService.create(role);

    operation.subscribe({
      next: (savedRole) => {
        const message = this.isEditing() 
          ? `Rol "${savedRole.name}" actualizado exitosamente`
          : `Rol "${savedRole.name}" creado exitosamente`;
        
        this.notificationService.success(message);
        this.closeModal();
        this.loadRoles();
      },
      error: (error) => {
        console.error('Error al guardar rol:', error);
        const action = this.isEditing() ? 'actualizar' : 'crear';
        this.notificationService.error(`Error al ${action} el rol`);
        this.isLoading.set(false);
      }
    });
  }

  /**
   * Elimina un rol
   */
  deleteRole(role: Role): void {
    if (!role.roleId) {
      this.notificationService.error('ID de rol no válido');
      return;
    }

    const confirmMessage = `¿Está seguro de eliminar el rol "${role.name}"?\n\nEsta acción no se puede deshacer.`;
    
    if (!confirm(confirmMessage)) {
      return;
    }

    this.isLoading.set(true);

    this.roleService.delete(role.roleId).subscribe({
      next: () => {
        this.notificationService.success(`Rol "${role.name}" eliminado exitosamente`);
        this.loadRoles();
      },
      error: (error) => {
        console.error('Error al eliminar rol:', error);
        
        if (error.status === 409 || error.status === 500) {
          this.notificationService.error('No se puede eliminar el rol porque está siendo usado por usuarios');
        } else {
          this.notificationService.error('Error al eliminar el rol');
        }
        
        this.isLoading.set(false);
      }
    });
  }

  /**
   * Actualiza el nombre del rol en el formulario
   */
  updateRoleName(name: string): void {
    this.currentRole.update(role => ({ ...role, name }));
  }

  /**
   * TrackBy function para optimizar el renderizado de la tabla
   */
  trackByRoleId(index: number, role: Role): number | undefined {
    return role.roleId;
  }
}

