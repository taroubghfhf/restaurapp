import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TableService, StatusService, NotificationService } from '../../../services';
import { Table, Status } from '../../../shared/models';
import { LoadingComponent } from '../../../shared/components';

@Component({
  selector: 'app-table-management',
  standalone: true,
  imports: [CommonModule, FormsModule, LoadingComponent],
  templateUrl: './table-management.component.html',
  styleUrl: './table-management.component.scss'
})
export class TableManagementComponent implements OnInit {
  private tableService = inject(TableService);
  private statusService = inject(StatusService);
  private notificationService = inject(NotificationService);

  tables = signal<Table[]>([]);
  statuses = signal<Status[]>([]);
  isLoading = signal(false);
  showModal = signal(false);
  isEditing = signal(false);
  
  currentTable = signal<Table>({
    capacity: 2,
    location: 1,
    status: { statusId: undefined, name: '' }
  });

  ngOnInit(): void {
    this.loadTables();
    this.loadStatuses();
  }

  /**
   * Carga todas las mesas desde el backend
   */
  loadTables(): void {
    this.isLoading.set(true);
    
    this.tableService.getAll().subscribe({
      next: (tables) => {
        this.tables.set(tables);
        this.isLoading.set(false);
      },
      error: (error) => {
        console.error('Error al cargar mesas:', error);
        this.notificationService.error('Error al cargar las mesas');
        this.isLoading.set(false);
      }
    });
  }

  /**
   * Carga todos los estados disponibles
   */
  loadStatuses(): void {
    this.statusService.getAll().subscribe({
      next: (statuses) => {
        this.statuses.set(statuses);
      },
      error: (error) => {
        console.error('Error al cargar estados:', error);
        this.notificationService.error('Error al cargar los estados');
      }
    });
  }

  /**
   * Abre el modal para crear una nueva mesa
   */
  openCreateModal(): void {
    this.currentTable.set({
      capacity: 2,
      location: 1,
      status: { statusId: undefined, name: '' }
    });
    this.isEditing.set(false);
    this.showModal.set(true);
  }

  /**
   * Abre el modal para editar una mesa existente
   */
  openEditModal(table: Table): void {
    this.currentTable.set({
      ...table,
      status: { ...table.status }
    });
    this.isEditing.set(true);
    this.showModal.set(true);
  }

  /**
   * Cierra el modal y resetea el formulario
   */
  closeModal(): void {
    this.showModal.set(false);
    this.currentTable.set({
      capacity: 2,
      location: 1,
      status: { statusId: undefined, name: '' }
    });
    this.isEditing.set(false);
  }

  /**
   * Guarda una mesa (crear o actualizar)
   */
  saveTable(): void {
    const table = this.currentTable();
    
    // Validaciones
    if (table.capacity < 1) {
      this.notificationService.warning('La capacidad debe ser al menos 1 persona');
      return;
    }

    if (table.capacity > 20) {
      this.notificationService.warning('La capacidad no puede exceder 20 personas');
      return;
    }

    if (table.location < 1) {
      this.notificationService.warning('La ubicación debe ser mayor a 0');
      return;
    }

    if (!table.status.statusId) {
      this.notificationService.warning('Debe seleccionar un estado');
      return;
    }

    this.isLoading.set(true);

    // Preparar mesa para enviar
    const tableToSave: Table = {
      ...table,
      status: {
        statusId: table.status.statusId,
        name: table.status.name || ''
      }
    };

    const operation = this.isEditing()
      ? this.tableService.update(tableToSave)
      : this.tableService.create(tableToSave);

    operation.subscribe({
      next: (savedTable) => {
        const action = this.isEditing() ? 'actualizada' : 'creada';
        this.notificationService.success(`Mesa #${savedTable.tableId} ${action} exitosamente`);
        this.closeModal();
        this.loadTables();
      },
      error: (error) => {
        console.error('Error al guardar mesa:', error);
        const action = this.isEditing() ? 'actualizar' : 'crear';
        this.notificationService.error(`Error al ${action} la mesa`);
        this.isLoading.set(false);
      }
    });
  }

  /**
   * Elimina una mesa
   */
  deleteTable(table: Table): void {
    if (!table.tableId) {
      this.notificationService.error('ID de mesa no válido');
      return;
    }

    const confirmMessage = `¿Está seguro de eliminar la Mesa #${table.tableId}?\n\nCapacidad: ${table.capacity} personas\nUbicación: Área ${table.location}\nEstado: ${table.status.name}\n\nEsta acción no se puede deshacer.`;
    
    if (!confirm(confirmMessage)) {
      return;
    }

    this.isLoading.set(true);

    this.tableService.delete(table.tableId).subscribe({
      next: () => {
        this.notificationService.success(`Mesa #${table.tableId} eliminada exitosamente`);
        this.loadTables();
      },
      error: (error) => {
        console.error('Error al eliminar mesa:', error);
        
        if (error.status === 409 || error.status === 500) {
          this.notificationService.error('No se puede eliminar la mesa porque tiene órdenes asociadas');
        } else {
          this.notificationService.error('Error al eliminar la mesa');
        }
        
        this.isLoading.set(false);
      }
    });
  }

  /**
   * Actualiza la capacidad de la mesa
   */
  updateTableCapacity(capacity: string): void {
    const numCapacity = parseInt(capacity) || 2;
    this.currentTable.update(table => ({ ...table, capacity: numCapacity }));
  }

  /**
   * Actualiza la ubicación de la mesa
   */
  updateTableLocation(location: string): void {
    const numLocation = parseInt(location) || 1;
    this.currentTable.update(table => ({ ...table, location: numLocation }));
  }

  /**
   * Actualiza el estado de la mesa
   */
  updateTableStatus(statusId: string): void {
    const selectedStatus = this.statuses().find(s => s.statusId?.toString() === statusId);
    if (selectedStatus) {
      this.currentTable.update(table => ({ 
        ...table, 
        status: { statusId: selectedStatus.statusId, name: selectedStatus.name } 
      }));
    }
  }

  /**
   * TrackBy function para optimizar el renderizado de la tabla
   */
  trackByTableId(index: number, table: Table): number | undefined {
    return table.tableId;
  }

  /**
   * Obtiene el ícono según el estado
   */
  getStatusIcon(statusName: string): string {
    const status = statusName.toLowerCase();
    if (status.includes('disponible') || status.includes('available')) return '✅';
    if (status.includes('ocupado') || status.includes('occupied')) return '🔒';
    if (status.includes('reservado') || status.includes('reserved')) return '📅';
    if (status.includes('limpieza') || status.includes('cleaning')) return '🧹';
    if (status.includes('mantenimiento') || status.includes('maintenance')) return '🔧';
    return '🪑';
  }

  /**
   * Obtiene la clase CSS según el estado
   */
  getStatusClass(statusName: string): string {
    const status = statusName.toLowerCase();
    if (status.includes('disponible') || status.includes('available')) return 'status-available';
    if (status.includes('ocupado') || status.includes('occupied')) return 'status-occupied';
    if (status.includes('reservado') || status.includes('reserved')) return 'status-reserved';
    if (status.includes('limpieza') || status.includes('cleaning')) return 'status-cleaning';
    if (status.includes('mantenimiento') || status.includes('maintenance')) return 'status-maintenance';
    return 'status-default';
  }

  /**
   * Obtiene el ícono según la capacidad
   */
  getCapacityIcon(capacity: number): string {
    if (capacity <= 2) return '👥';
    if (capacity <= 4) return '👨‍👩‍👧‍👦';
    if (capacity <= 6) return '👪';
    return '👨‍👩‍👧‍👦👨‍👩‍👧‍👦';
  }

  /**
   * Obtiene el texto del área según la ubicación
   */
  getLocationName(location: number): string {
    const areas: { [key: number]: string } = {
      1: 'Terraza',
      2: 'Interior',
      3: 'VIP',
      4: 'Barra',
      5: 'Exterior'
    };
    return areas[location] || `Área ${location}`;
  }
}



