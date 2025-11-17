import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ProductService, CategoryService, NotificationService } from '../../../services';
import { Product, Category } from '../../../shared/models';
import { LoadingComponent } from '../../../shared/components';

@Component({
  selector: 'app-product-management',
  standalone: true,
  imports: [CommonModule, FormsModule, LoadingComponent],
  templateUrl: './product-management.component.html',
  styleUrl: './product-management.component.scss'
})
export class ProductManagementComponent implements OnInit {
  private productService = inject(ProductService);
  private categoryService = inject(CategoryService);
  private notificationService = inject(NotificationService);

  products = signal<Product[]>([]);
  categories = signal<Category[]>([]);
  isLoading = signal(false);
  showModal = signal(false);
  isEditing = signal(false);
  
  currentProduct = signal<Product>({
    name: '',
    price: 0,
    stock: 0,
    status: true,
    category: { categoryId: undefined, name: '' }
  });

  ngOnInit(): void {
    this.loadProducts();
    this.loadCategories();
  }

  /**
   * Carga todos los productos desde el backend
   */
  loadProducts(): void {
    this.isLoading.set(true);
    
    this.productService.getAll().subscribe({
      next: (products) => {
        this.products.set(products);
        this.isLoading.set(false);
      },
      error: (error) => {
        console.error('Error al cargar productos:', error);
        this.notificationService.error('Error al cargar los productos');
        this.isLoading.set(false);
      }
    });
  }

  /**
   * Carga todas las categorías disponibles
   */
  loadCategories(): void {
    this.categoryService.getAll().subscribe({
      next: (categories) => {
        this.categories.set(categories);
      },
      error: (error) => {
        console.error('Error al cargar categorías:', error);
        this.notificationService.error('Error al cargar las categorías');
      }
    });
  }

  /**
   * Abre el modal para crear un nuevo producto
   */
  openCreateModal(): void {
    this.currentProduct.set({
      name: '',
      price: 0,
      stock: 0,
      status: true,
      category: { categoryId: undefined, name: '' }
    });
    this.isEditing.set(false);
    this.showModal.set(true);
  }

  /**
   * Abre el modal para editar un producto existente
   */
  openEditModal(product: Product): void {
    this.currentProduct.set({
      ...product,
      category: { ...product.category }
    });
    this.isEditing.set(true);
    this.showModal.set(true);
  }

  /**
   * Cierra el modal y resetea el formulario
   */
  closeModal(): void {
    this.showModal.set(false);
    this.currentProduct.set({
      name: '',
      price: 0,
      stock: 0,
      status: true,
      category: { categoryId: undefined, name: '' }
    });
    this.isEditing.set(false);
  }

  /**
   * Guarda un producto (crear o actualizar)
   */
  saveProduct(): void {
    const product = this.currentProduct();
    
    // Validaciones
    if (!product.name.trim()) {
      this.notificationService.warning('El nombre del producto es requerido');
      return;
    }

    if (product.name.length > 50) {
      this.notificationService.warning('El nombre no puede exceder 50 caracteres');
      return;
    }

    if (product.price < 0) {
      this.notificationService.warning('El precio debe ser mayor o igual a 0');
      return;
    }

    if (product.stock < 0) {
      this.notificationService.warning('El stock debe ser mayor o igual a 0');
      return;
    }

    if (!product.category.categoryId) {
      this.notificationService.warning('Debe seleccionar una categoría');
      return;
    }

    this.isLoading.set(true);

    // Preparar producto para enviar
    const productToSave: Product = {
      ...product,
      category: {
        categoryId: product.category.categoryId,
        name: product.category.name || ''
      }
    };

    const operation = this.isEditing()
      ? this.productService.update(productToSave)
      : this.productService.create(productToSave);

    operation.subscribe({
      next: (savedProduct) => {
        const action = this.isEditing() ? 'actualizado' : 'creado';
        this.notificationService.success(`Producto "${savedProduct.name}" ${action} exitosamente`);
        this.closeModal();
        this.loadProducts();
      },
      error: (error) => {
        console.error('Error al guardar producto:', error);
        const action = this.isEditing() ? 'actualizar' : 'crear';
        this.notificationService.error(`Error al ${action} el producto`);
        this.isLoading.set(false);
      }
    });
  }

  /**
   * Elimina un producto
   */
  deleteProduct(product: Product): void {
    if (!product.productId) {
      this.notificationService.error('ID de producto no válido');
      return;
    }

    const confirmMessage = `¿Está seguro de eliminar el producto "${product.name}"?\n\nCategoría: ${product.category.name}\nPrecio: $${product.price}\nStock: ${product.stock}\n\nEsta acción no se puede deshacer.`;
    
    if (!confirm(confirmMessage)) {
      return;
    }

    this.isLoading.set(true);

    this.productService.delete(product.productId).subscribe({
      next: () => {
        this.notificationService.success(`Producto "${product.name}" eliminado exitosamente`);
        this.loadProducts();
      },
      error: (error) => {
        console.error('Error al eliminar producto:', error);
        
        if (error.status === 409 || error.status === 500) {
          this.notificationService.error('No se puede eliminar el producto porque está en uso');
        } else {
          this.notificationService.error('Error al eliminar el producto');
        }
        
        this.isLoading.set(false);
      }
    });
  }

  /**
   * Actualiza el nombre del producto
   */
  updateProductName(name: string): void {
    this.currentProduct.update(product => ({ ...product, name }));
  }

  /**
   * Actualiza el precio del producto
   */
  updateProductPrice(price: string): void {
    const numPrice = parseFloat(price) || 0;
    this.currentProduct.update(product => ({ ...product, price: numPrice }));
  }

  /**
   * Actualiza el stock del producto
   */
  updateProductStock(stock: string): void {
    const numStock = parseInt(stock) || 0;
    this.currentProduct.update(product => ({ ...product, stock: numStock }));
  }

  /**
   * Toggle del estado del producto
   */
  toggleProductStatus(): void {
    this.currentProduct.update(product => ({ ...product, status: !product.status }));
  }

  /**
   * Actualiza la categoría del producto
   */
  updateProductCategory(categoryId: string): void {
    const selectedCategory = this.categories().find(c => c.categoryId?.toString() === categoryId);
    if (selectedCategory) {
      this.currentProduct.update(product => ({ 
        ...product, 
        category: { categoryId: selectedCategory.categoryId, name: selectedCategory.name } 
      }));
    }
  }

  /**
   * TrackBy function para optimizar el renderizado de la tabla
   */
  trackByProductId(index: number, product: Product): number | undefined {
    return product.productId;
  }

  /**
   * Formatea el precio para mostrar
   */
  formatPrice(price: number): string {
    return price.toFixed(2);
  }

  /**
   * Obtiene la clase CSS según el estado del producto
   */
  getStatusClass(status: boolean): string {
    return status ? 'status-active' : 'status-inactive';
  }

  /**
   * Obtiene el texto según el estado del producto
   */
  getStatusText(status: boolean): string {
    return status ? 'Activo' : 'Inactivo';
  }

  /**
   * Obtiene el ícono según el estado del producto
   */
  getStatusIcon(status: boolean): string {
    return status ? '✅' : '🔴';
  }
}



