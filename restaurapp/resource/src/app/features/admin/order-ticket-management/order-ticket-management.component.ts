import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { OrderTicketService, TableService, UserService, StatusService, NotificationService } from '../../../services';
import { OrderTicket, Table, User, Status } from '../../../shared/models';
import { LoadingComponent } from '../../../shared/components';

@Component({
  selector: 'app-order-ticket-management',
  standalone: true,
  imports: [CommonModule, FormsModule, LoadingComponent],
  templateUrl: './order-ticket-management.component.html',
  styleUrl: './order-ticket-management.component.scss'
})
export class OrderTicketManagementComponent implements OnInit {
  private orderTicketService = inject(OrderTicketService);
  private tableService = inject(TableService);
  private userService = inject(UserService);
  private statusService = inject(StatusService);
  private notificationService = inject(NotificationService);
  private router = inject(Router);

  orderTickets = signal<OrderTicket[]>([]);
  tables = signal<Table[]>([]);
  users = signal<User[]>([]);
  statuses = signal<Status[]>([]);
  isLoading = signal(false);
  showModal = signal(false);
  isEditing = signal(false);

  currentOrderTicket = signal<OrderTicket>({
    date: new Date().toISOString(),
    table: { tableId: undefined, capacity: 0, location: 0, status: { statusId: undefined, name: '' } },
    waiter: { userId: undefined, name: '', email: '', password: '', role: { roleId: undefined, name: '' } },
    chef: { userId: undefined, name: '', email: '', password: '', role: { roleId: undefined, name: '' } },
    status: { statusId: undefined, name: '' }
  });

  ngOnInit(): void {
    this.loadOrderTickets();
    this.loadTables();
    this.loadUsers();
    this.loadStatuses();
  }

  /**
   * Carga todas las órdenes desde el backend
   */
  loadOrderTickets(): void {
    this.isLoading.set(true);
    
    this.orderTicketService.getAll().subscribe({
      next: (orderTickets) => {
        this.orderTickets.set(orderTickets);
        this.isLoading.set(false);
      },
      error: (error) => {
        console.error('Error al cargar órdenes:', error);
        this.notificationService.error('Error al cargar las órdenes');
        this.isLoading.set(false);
      }
    });
  }

  /**
   * Carga todas las mesas disponibles
   */
  loadTables(): void {
    this.tableService.getAll().subscribe({
      next: (tables) => {
        this.tables.set(tables);
      },
      error: (error) => {
        console.error('Error al cargar mesas:', error);
        this.notificationService.error('Error al cargar las mesas');
      }
    });
  }

  /**
   * Carga todos los usuarios (meseros y chefs)
   */
  loadUsers(): void {
    this.userService.getAll().subscribe({
      next: (users) => {
        this.users.set(users);
      },
      error: (error) => {
        console.error('Error al cargar usuarios:', error);
        this.notificationService.error('Error al cargar los usuarios');
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
   * Abre el modal para crear una nueva orden
   */
  openCreateModal(): void {
    this.currentOrderTicket.set({
      date: new Date().toISOString(),
      table: { tableId: undefined, capacity: 0, location: 0, status: { statusId: undefined, name: '' } },
      waiter: { userId: undefined, name: '', email: '', password: '', role: { roleId: undefined, name: '' } },
      chef: { userId: undefined, name: '', email: '', password: '', role: { roleId: undefined, name: '' } },
      status: { statusId: undefined, name: '' }
    });
    this.isEditing.set(false);
    this.showModal.set(true);
  }

  /**
   * Abre el modal para editar una orden existente
   */
  openEditModal(orderTicket: OrderTicket): void {
    this.currentOrderTicket.set({
      ...orderTicket,
      table: { ...orderTicket.table },
      waiter: { ...orderTicket.waiter },
      chef: { ...orderTicket.chef },
      status: { ...orderTicket.status }
    });
    this.isEditing.set(true);
    this.showModal.set(true);
  }

  /**
   * Cierra el modal y resetea el formulario
   */
  closeModal(): void {
    this.showModal.set(false);
    this.currentOrderTicket.set({
      date: new Date().toISOString(),
      table: { tableId: undefined, capacity: 0, location: 0, status: { statusId: undefined, name: '' } },
      waiter: { userId: undefined, name: '', email: '', password: '', role: { roleId: undefined, name: '' } },
      chef: { userId: undefined, name: '', email: '', password: '', role: { roleId: undefined, name: '' } },
      status: { statusId: undefined, name: '' }
    });
    this.isEditing.set(false);
  }

  /**
   * Guarda una orden (crear o actualizar)
   */
  saveOrderTicket(): void {
    const orderTicket = this.currentOrderTicket();
    
    // Validaciones
    if (!orderTicket.table.tableId) {
      this.notificationService.warning('Debe seleccionar una mesa');
      return;
    }

    if (!orderTicket.waiter.userId) {
      this.notificationService.warning('Debe seleccionar un mesero');
      return;
    }

    if (!orderTicket.chef.userId) {
      this.notificationService.warning('Debe seleccionar un chef');
      return;
    }

    if (!orderTicket.status.statusId) {
      this.notificationService.warning('Debe seleccionar un estado');
      return;
    }

    if (!orderTicket.date) {
      this.notificationService.warning('La fecha es requerida');
      return;
    }

    this.isLoading.set(true);

    // Preparar order ticket para enviar
    const orderTicketToSave: OrderTicket = {
      ...orderTicket,
      date: orderTicket.date,
      table: {
        tableId: orderTicket.table.tableId,
        capacity: orderTicket.table.capacity || 0,
        location: orderTicket.table.location || 0,
        status: orderTicket.table.status || { statusId: undefined, name: '' }
      },
      waiter: {
        userId: orderTicket.waiter.userId,
        name: orderTicket.waiter.name || '',
        email: orderTicket.waiter.email || '',
        password: orderTicket.waiter.password || '',
        role: orderTicket.waiter.role || { roleId: undefined, name: '' }
      },
      chef: {
        userId: orderTicket.chef.userId,
        name: orderTicket.chef.name || '',
        email: orderTicket.chef.email || '',
        password: orderTicket.chef.password || '',
        role: orderTicket.chef.role || { roleId: undefined, name: '' }
      },
      status: {
        statusId: orderTicket.status.statusId,
        name: orderTicket.status.name || ''
      }
    };

    const operation = this.isEditing()
      ? this.orderTicketService.update(orderTicketToSave)
      : this.orderTicketService.create(orderTicketToSave);

    operation.subscribe({
      next: (savedOrderTicket) => {
        const action = this.isEditing() ? 'actualizada' : 'creada';
        this.notificationService.success(`Orden #${savedOrderTicket.orderTicketId} ${action} exitosamente`);
        this.closeModal();
        this.loadOrderTickets();
      },
      error: (error) => {
        console.error('Error al guardar orden:', error);
        const action = this.isEditing() ? 'actualizar' : 'crear';
        this.notificationService.error(`Error al ${action} la orden`);
        this.isLoading.set(false);
      }
    });
  }

  /**
   * Elimina una orden
   */
  deleteOrderTicket(orderTicket: OrderTicket): void {
    if (!orderTicket.orderTicketId) {
      this.notificationService.error('ID de orden no válido');
      return;
    }

    const confirmMessage = `¿Está seguro de eliminar la Orden #${orderTicket.orderTicketId}?\n\nMesa: ${this.getTableName(orderTicket.table)}\nMesero: ${orderTicket.waiter.name}\nChef: ${orderTicket.chef.name}\nEstado: ${orderTicket.status.name}\n\nEsta acción eliminará también todos los items de esta orden.`;
    
    if (!confirm(confirmMessage)) {
      return;
    }

    this.isLoading.set(true);

    this.orderTicketService.delete(orderTicket.orderTicketId).subscribe({
      next: () => {
        this.notificationService.success(`Orden #${orderTicket.orderTicketId} eliminada exitosamente`);
        this.loadOrderTickets();
      },
      error: (error) => {
        console.error('Error al eliminar orden:', error);
        
        if (error.status === 409 || error.status === 500) {
          this.notificationService.error('No se puede eliminar la orden porque tiene items asociados');
        } else {
          this.notificationService.error('Error al eliminar la orden');
        }
        
        this.isLoading.set(false);
      }
    });
  }

  /**
   * Navega a la gestión de items de una orden
   */
  viewOrderItems(orderTicket: OrderTicket): void {
    this.router.navigate(['/admin/order-items'], { 
      queryParams: { orderId: orderTicket.orderTicketId } 
    });
  }

  /**
   * Actualiza la mesa seleccionada
   */
  updateTable(tableId: string): void {
    const selectedTable = this.tables().find(t => t.tableId?.toString() === tableId);
    if (selectedTable) {
      this.currentOrderTicket.update(ticket => ({
        ...ticket,
        table: { ...selectedTable }
      }));
    }
  }

  /**
   * Actualiza el mesero seleccionado
   */
  updateWaiter(waiterId: string): void {
    const selectedWaiter = this.users().find(u => u.userId?.toString() === waiterId);
    if (selectedWaiter) {
      this.currentOrderTicket.update(ticket => ({
        ...ticket,
        waiter: { ...selectedWaiter }
      }));
    }
  }

  /**
   * Actualiza el chef seleccionado
   */
  updateChef(chefId: string): void {
    const selectedChef = this.users().find(u => u.userId?.toString() === chefId);
    if (selectedChef) {
      this.currentOrderTicket.update(ticket => ({
        ...ticket,
        chef: { ...selectedChef }
      }));
    }
  }

  /**
   * Actualiza el estado seleccionado
   */
  updateStatus(statusId: string): void {
    const selectedStatus = this.statuses().find(s => s.statusId?.toString() === statusId);
    if (selectedStatus) {
      this.currentOrderTicket.update(ticket => ({
        ...ticket,
        status: { ...selectedStatus }
      }));
    }
  }

  /**
   * Actualiza la fecha
   */
  updateDate(date: string): void {
    this.currentOrderTicket.update(ticket => ({
      ...ticket,
      date: date
    }));
  }

  /**
   * Obtiene el nombre de la mesa
   */
  getTableName(table: Table): string {
    return table.tableId ? `Mesa #${table.tableId}` : 'Sin mesa';
  }

  /**
   * Obtiene la ubicación de la mesa
   */
  getTableLocation(table: Table): string {
    const areas: { [key: number]: string } = {
      1: 'Terraza',
      2: 'Interior',
      3: 'VIP',
      4: 'Barra',
      5: 'Exterior'
    };
    return areas[table.location] || `Área ${table.location}`;
  }

  /**
   * Formatea la fecha
   */
  formatDate(dateString: string): string {
    const date = new Date(dateString);
    return new Intl.DateTimeFormat('es-CO', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    }).format(date);
  }

  /**
   * Obtiene el ícono según el estado
   */
  getStatusIcon(statusName: string): string {
    const status = statusName.toLowerCase();
    if (status.includes('pendiente') || status.includes('pending')) return '⏳';
    if (status.includes('preparando') || status.includes('preparing')) return '👨‍🍳';
    if (status.includes('listo') || status.includes('ready')) return '✅';
    if (status.includes('entregado') || status.includes('delivered')) return '🍽️';
    if (status.includes('completado') || status.includes('completed')) return '✔️';
    if (status.includes('cancelado') || status.includes('cancelled')) return '❌';
    return '📋';
  }

  /**
   * Obtiene la clase CSS según el estado
   */
  getStatusClass(statusName: string): string {
    const status = statusName.toLowerCase();
    if (status.includes('pendiente') || status.includes('pending')) return 'status-pending';
    if (status.includes('preparando') || status.includes('preparing')) return 'status-preparing';
    if (status.includes('listo') || status.includes('ready')) return 'status-ready';
    if (status.includes('entregado') || status.includes('delivered')) return 'status-delivered';
    if (status.includes('completado') || status.includes('completed')) return 'status-completed';
    if (status.includes('cancelado') || status.includes('cancelled')) return 'status-cancelled';
    return 'status-default';
  }

  /**
   * Filtra usuarios por rol
   */
  getWaiters(): User[] {
    return this.users().filter(u => u.role.name === 'Waiter' || u.role.name === 'Mesero');
  }

  getChefs(): User[] {
    return this.users().filter(u => u.role.name === 'Chef' || u.role.name === 'Cocinero');
  }

  /**
   * TrackBy function para optimizar el renderizado
   */
  trackByOrderTicketId(index: number, orderTicket: OrderTicket): number | undefined {
    return orderTicket.orderTicketId;
  }
}



