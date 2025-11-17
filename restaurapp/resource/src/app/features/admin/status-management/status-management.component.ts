import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { StatusService, NotificationService } from '../../../services';
import { Status } from '../../../shared/models';
import { LoadingComponent } from '../../../shared/components';

@Component({
  selector: 'app-status-management',
  standalone: true,
  imports: [CommonModule, FormsModule, LoadingComponent],
  templateUrl: './status-management.component.html',
  styleUrl: './status-management.component.scss'
})
export class StatusManagementComponent implements OnInit {
  private statusService = inject(StatusService);
  private notificationService = inject(NotificationService);

  statuses = signal<Status[]>([]);
  isLoading = signal(false);
  showModal = signal(false);
  isEditing = signal(false);
  
  currentStatus = signal<Status>({
    name: ''
  });

  ngOnInit(): void {
    this.loadStatuses();
  }

  /**
   * Carga todos los estados desde el backend
   */
  loadStatuses(): void {
    this.isLoading.set(true);
    
    this.statusService.getAll().subscribe({
      next: (statuses) => {
        this.statuses.set(statuses);
        this.isLoading.set(false);
      },
      error: (error) => {
        console.error('Error al cargar estados:', error);
        this.notificationService.error('Error al cargar los estados');
        this.isLoading.set(false);
      }
    });
  }

  /**
   * Abre el modal para crear un nuevo estado
   */
  openCreateModal(): void {
    this.currentStatus.set({ name: '' });
    this.isEditing.set(false);
    this.showModal.set(true);
  }

  /**
   * Abre el modal para editar un estado existente
   */
  openEditModal(status: Status): void {
    this.currentStatus.set({ ...status });
    this.isEditing.set(true);
    this.showModal.set(true);
  }

  /**
   * Cierra el modal y resetea el formulario
   */
  closeModal(): void {
    this.showModal.set(false);
    this.currentStatus.set({ name: '' });
    this.isEditing.set(false);
  }

  /**
   * Guarda un estado (crear o actualizar)
   */
  saveStatus(): void {
    const status = this.currentStatus();
    
    if (!status.name.trim()) {
      this.notificationService.warning('El nombre del estado es requerido');
      return;
    }

    if (status.name.length > 10) {
      this.notificationService.warning('El nombre no puede exceder 10 caracteres');
      return;
    }

    this.isLoading.set(true);

    const operation = this.isEditing()
      ? this.statusService.update(status)
      : this.statusService.create(status);

    operation.subscribe({
      next: (savedStatus) => {
        const message = this.isEditing() 
          ? `Estado "${savedStatus.name}" actualizado exitosamente`
          : `Estado "${savedStatus.name}" creado exitosamente`;
        
        this.notificationService.success(message);
        this.closeModal();
        this.loadStatuses();
      },
      error: (error) => {
        console.error('Error al guardar estado:', error);
        const action = this.isEditing() ? 'actualizar' : 'crear';
        this.notificationService.error(`Error al ${action} el estado`);
        this.isLoading.set(false);
      }
    });
  }

  /**
   * Elimina un estado
   */
  deleteStatus(status: Status): void {
    if (!status.statusId) {
      this.notificationService.error('ID de estado no válido');
      return;
    }

    const confirmMessage = `¿Está seguro de eliminar el estado "${status.name}"?\n\nEsta acción no se puede deshacer y afectará todos los elementos que usan este estado.`;
    
    if (!confirm(confirmMessage)) {
      return;
    }

    this.isLoading.set(true);

    this.statusService.delete(status.statusId).subscribe({
      next: () => {
        this.notificationService.success(`Estado "${status.name}" eliminado exitosamente`);
        this.loadStatuses();
      },
      error: (error) => {
        console.error('Error al eliminar estado:', error);
        
        if (error.status === 409 || error.status === 500) {
          this.notificationService.error('No se puede eliminar el estado porque está siendo usado');
        } else {
          this.notificationService.error('Error al eliminar el estado');
        }
        
        this.isLoading.set(false);
      }
    });
  }

  /**
   * Actualiza el nombre del estado en el formulario
   */
  updateStatusName(name: string): void {
    this.currentStatus.update(status => ({ ...status, name }));
  }

  /**
   * TrackBy function para optimizar el renderizado de la tabla
   */
  trackByStatusId(index: number, status: Status): number | undefined {
    return status.statusId;
  }

  /**
   * Obtiene la clase CSS según el nombre del estado
   */
  getStatusClass(name: string): string {
    const statusName = name.toLowerCase();
    
    // Estados comunes
    if (statusName === 'active' || statusName === 'activo' || statusName === 'disponible' || statusName === 'available') {
      return 'status-active';
    }
    if (statusName === 'pending' || statusName === 'pendiente' || statusName === 'en espera') {
      return 'status-pending';
    }
    if (statusName === 'completed' || statusName === 'completado' || statusName === 'finalizado') {
      return 'status-completed';
    }
    if (statusName === 'cancelled' || statusName === 'cancelado') {
      return 'status-cancelled';
    }
    if (statusName === 'inactive' || statusName === 'inactivo') {
      return 'status-inactive';
    }
    if (statusName === 'preparing' || statusName === 'preparando' || statusName === 'en preparación') {
      return 'status-preparing';
    }
    if (statusName === 'ready' || statusName === 'listo' || statusName === 'preparado') {
      return 'status-ready';
    }
    if (statusName === 'occupied' || statusName === 'ocupado' || statusName === 'ocupada') {
      return 'status-occupied';
    }
    
    return 'status-default';
  }

  /**
   * Obtiene el ícono según el nombre del estado
   */
  getStatusIcon(name: string): string {
    const statusName = name.toLowerCase();
    
    if (statusName === 'active' || statusName === 'activo' || statusName === 'disponible' || statusName === 'available') {
      return '✅';
    }
    if (statusName === 'pending' || statusName === 'pendiente' || statusName === 'en espera') {
      return '⏳';
    }
    if (statusName === 'completed' || statusName === 'completado' || statusName === 'finalizado') {
      return '🎉';
    }
    if (statusName === 'cancelled' || statusName === 'cancelado') {
      return '❌';
    }
    if (statusName === 'inactive' || statusName === 'inactivo') {
      return '🔴';
    }
    if (statusName === 'preparing' || statusName === 'preparando' || statusName === 'en preparación') {
      return '👨‍🍳';
    }
    if (statusName === 'ready' || statusName === 'listo' || statusName === 'preparado') {
      return '✨';
    }
    if (statusName === 'occupied' || statusName === 'ocupado' || statusName === 'ocupada') {
      return '🔒';
    }
    
    return '🏷️';
  }
}



