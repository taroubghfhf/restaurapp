import { Component, OnInit, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { OrderItemService, OrderTicketService, ProductService, NotificationService } from '../../../services';
import { OrderItem, OrderTicket, Product } from '../../../shared/models';
import { LoadingComponent } from '../../../shared/components';

@Component({
  selector: 'app-order-item-management',
  standalone: true,
  imports: [CommonModule, FormsModule, LoadingComponent],
  templateUrl: './order-item-management.component.html',
  styleUrl: './order-item-management.component.scss'
})
export class OrderItemManagementComponent implements OnInit {
  private orderItemService = inject(OrderItemService);
  private orderTicketService = inject(OrderTicketService);
  private productService = inject(ProductService);
  private notificationService = inject(NotificationService);

  orderItems = signal<OrderItem[]>([]);
  orderTickets = signal<OrderTicket[]>([]);
  products = signal<Product[]>([]);
  isLoading = signal(false);
  showModal = signal(false);
  isEditing = signal(false);

  currentOrderItem = signal<OrderItem>({
    orderTicket: { 
      orderTicketId: undefined, 
      date: '', 
      table: { tableId: undefined, capacity: 0, location: 0, status: { statusId: undefined, name: '' } },
      waiter: { userId: undefined, name: '', email: '', password: '', role: { roleId: undefined, name: '' } },
      chef: { userId: undefined, name: '', email: '', password: '', role: { roleId: undefined, name: '' } },
      status: { statusId: undefined, name: '' }
    },
    product: { productId: undefined, name: '', category: { categoryId: undefined, name: '' }, price: 0, stock: 0, status: true },
    quantity: 1,
    subtotal: 0
  });

  // Computed para calcular el subtotal
  subtotal = computed(() => {
    const item = this.currentOrderItem();
    // El subtotal se calcula como cantidad * precio del producto
    return item.quantity * (item.product.price || 0);
  });

  ngOnInit(): void {
    this.loadOrderItems();
    this.loadOrderTickets();
    this.loadProducts();
  }

  /**
   * Carga todos los order items desde el backend
   */
  loadOrderItems(): void {
    this.isLoading.set(true);
    
    this.orderItemService.getAll().subscribe({
      next: (orderItems) => {
        this.orderItems.set(orderItems);
        this.isLoading.set(false);
      },
      error: (error) => {
        console.error('Error al cargar items de orden:', error);
        this.notificationService.error('Error al cargar los items de orden');
        this.isLoading.set(false);
      }
    });
  }

  /**
   * Carga todas las órdenes disponibles
   */
  loadOrderTickets(): void {
    this.orderTicketService.getAll().subscribe({
      next: (orderTickets) => {
        this.orderTickets.set(orderTickets);
      },
      error: (error) => {
        console.error('Error al cargar órdenes:', error);
        this.notificationService.error('Error al cargar las órdenes');
      }
    });
  }

  /**
   * Carga todos los productos disponibles
   */
  loadProducts(): void {
    this.productService.getAll().subscribe({
      next: (products) => {
        // Solo productos activos con stock
        const activeProducts = products.filter(p => p.status && p.stock > 0);
        this.products.set(activeProducts);
      },
      error: (error) => {
        console.error('Error al cargar productos:', error);
        this.notificationService.error('Error al cargar los productos');
      }
    });
  }

  /**
   * Abre el modal para crear un nuevo item de orden
   */
  openCreateModal(): void {
    this.currentOrderItem.set({
      orderTicket: { 
        orderTicketId: undefined, 
        date: '', 
        table: { tableId: undefined, capacity: 0, location: 0, status: { statusId: undefined, name: '' } },
        waiter: { userId: undefined, name: '', email: '', password: '', role: { roleId: undefined, name: '' } },
        chef: { userId: undefined, name: '', email: '', password: '', role: { roleId: undefined, name: '' } },
        status: { statusId: undefined, name: '' }
      },
      product: { productId: undefined, name: '', category: { categoryId: undefined, name: '' }, price: 0, stock: 0, status: true },
      quantity: 1,
      subtotal: 0
    });
    this.isEditing.set(false);
    this.showModal.set(true);
  }

  /**
   * Abre el modal para editar un item de orden existente
   */
  openEditModal(orderItem: OrderItem): void {
    this.currentOrderItem.set({
      ...orderItem,
      orderTicket: { ...orderItem.orderTicket },
      product: { ...orderItem.product }
    });
    this.isEditing.set(true);
    this.showModal.set(true);
  }

  /**
   * Cierra el modal y resetea el formulario
   */
  closeModal(): void {
    this.showModal.set(false);
    this.currentOrderItem.set({
      orderTicket: { 
        orderTicketId: undefined, 
        date: '', 
        table: { tableId: undefined, capacity: 0, location: 0, status: { statusId: undefined, name: '' } },
        waiter: { userId: undefined, name: '', email: '', password: '', role: { roleId: undefined, name: '' } },
        chef: { userId: undefined, name: '', email: '', password: '', role: { roleId: undefined, name: '' } },
        status: { statusId: undefined, name: '' }
      },
      product: { productId: undefined, name: '', category: { categoryId: undefined, name: '' }, price: 0, stock: 0, status: true },
      quantity: 1,
      subtotal: 0
    });
    this.isEditing.set(false);
  }

  /**
   * Guarda un item de orden (crear o actualizar)
   */
  saveOrderItem(): void {
    const orderItem = this.currentOrderItem();
    
    // Validaciones
    if (!orderItem.orderTicket.orderTicketId) {
      this.notificationService.warning('Debe seleccionar una orden');
      return;
    }

    if (!orderItem.product.productId) {
      this.notificationService.warning('Debe seleccionar un producto');
      return;
    }

    if (orderItem.quantity < 1) {
      this.notificationService.warning('La cantidad debe ser al menos 1');
      return;
    }

    if (orderItem.quantity > 100) {
      this.notificationService.warning('La cantidad no puede exceder 100 unidades');
      return;
    }

    // Validar stock disponible
    const selectedProduct = this.products().find(p => p.productId === orderItem.product.productId);
    if (selectedProduct && orderItem.quantity > selectedProduct.stock) {
      this.notificationService.warning(`Stock insuficiente. Solo hay ${selectedProduct.stock} unidades disponibles`);
      return;
    }

    if (!selectedProduct || selectedProduct.price <= 0) {
      this.notificationService.warning('El producto debe tener un precio válido');
      return;
    }

    this.isLoading.set(true);

    // Calcular el subtotal
    const calculatedSubtotal = orderItem.quantity * selectedProduct.price;

    // Preparar order item para enviar
    const orderItemToSave: OrderItem = {
      ...orderItem,
      subtotal: calculatedSubtotal,
      orderTicket: {
        orderTicketId: orderItem.orderTicket.orderTicketId,
        date: orderItem.orderTicket.date || new Date().toISOString(),
        table: orderItem.orderTicket.table,
        waiter: orderItem.orderTicket.waiter,
        chef: orderItem.orderTicket.chef,
        status: orderItem.orderTicket.status
      },
      product: {
        productId: orderItem.product.productId,
        name: orderItem.product.name || '',
        category: orderItem.product.category || { categoryId: undefined, name: '' },
        price: orderItem.product.price || 0,
        stock: orderItem.product.stock || 0,
        status: orderItem.product.status ?? true
      }
    };

    const operation = this.isEditing()
      ? this.orderItemService.update(orderItemToSave)
      : this.orderItemService.create(orderItemToSave);

    operation.subscribe({
      next: (savedOrderItem) => {
        const action = this.isEditing() ? 'actualizado' : 'creado';
        this.notificationService.success(`Item de orden ${action} exitosamente`);
        this.closeModal();
        this.loadOrderItems();
      },
      error: (error) => {
        console.error('Error al guardar item de orden:', error);
        const action = this.isEditing() ? 'actualizar' : 'crear';
        this.notificationService.error(`Error al ${action} el item de orden`);
        this.isLoading.set(false);
      }
    });
  }

  /**
   * Elimina un item de orden
   */
  deleteOrderItem(orderItem: OrderItem): void {
    if (!orderItem.orderItemId) {
      this.notificationService.error('ID de item no válido');
      return;
    }

    const confirmMessage = `¿Está seguro de eliminar este item?\n\nProducto: ${orderItem.product.name}\nCantidad: ${orderItem.quantity}\nSubtotal: $${this.calculateSubtotal(orderItem).toFixed(2)}\n\nEsta acción no se puede deshacer.`;
    
    if (!confirm(confirmMessage)) {
      return;
    }

    this.isLoading.set(true);

    this.orderItemService.delete(orderItem.orderItemId).subscribe({
      next: () => {
        this.notificationService.success('Item de orden eliminado exitosamente');
        this.loadOrderItems();
      },
      error: (error) => {
        console.error('Error al eliminar item de orden:', error);
        this.notificationService.error('Error al eliminar el item de orden');
        this.isLoading.set(false);
      }
    });
  }

  /**
   * Actualiza la orden seleccionada
   */
  updateOrderTicket(orderTicketId: string): void {
    const selectedOrderTicket = this.orderTickets().find(o => o.orderTicketId?.toString() === orderTicketId);
    if (selectedOrderTicket) {
      this.currentOrderItem.update(item => ({
        ...item,
        orderTicket: { ...selectedOrderTicket }
      }));
    }
  }

  /**
   * Actualiza el producto seleccionado
   */
  updateProduct(productId: string): void {
    const selectedProduct = this.products().find(p => p.productId?.toString() === productId);
    if (selectedProduct) {
      this.currentOrderItem.update(item => ({
        ...item,
        product: { ...selectedProduct }
      }));
    }
  }

  /**
   * Actualiza la cantidad
   */
  updateQuantity(quantity: string): void {
    const numQuantity = parseInt(quantity) || 1;
    this.currentOrderItem.update(item => ({ ...item, quantity: numQuantity }));
  }

  /**
   * Calcula el subtotal de un item
   */
  calculateSubtotal(orderItem: OrderItem): number {
    return orderItem.subtotal;
  }

  /**
   * Obtiene el nombre de la mesa desde un order ticket
   */
  getTableName(orderTicket: OrderTicket): string {
    return orderTicket.table?.tableId ? `Mesa #${orderTicket.table.tableId}` : 'Sin mesa';
  }

  /**
   * Obtiene el nombre del mesero desde un order ticket
   */
  getUserName(orderTicket: OrderTicket): string {
    return orderTicket.waiter?.name || 'Sin mesero';
  }

  /**
   * TrackBy function para optimizar el renderizado
   */
  trackByOrderItemId(index: number, orderItem: OrderItem): number | undefined {
    return orderItem.orderItemId;
  }

  /**
   * Formatea números como moneda
   */
  formatCurrency(value: number): string {
    return new Intl.NumberFormat('es-CO', {
      style: 'currency',
      currency: 'COP',
      minimumFractionDigits: 0,
      maximumFractionDigits: 0
    }).format(value);
  }
}

