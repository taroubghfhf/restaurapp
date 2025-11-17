import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CategoryService, NotificationService } from '../../../services';
import { Category } from '../../../shared/models';
import { LoadingComponent } from '../../../shared/components';

@Component({
  selector: 'app-category-management',
  standalone: true,
  imports: [CommonModule, FormsModule, LoadingComponent],
  templateUrl: './category-management.component.html',
  styleUrl: './category-management.component.scss'
})
export class CategoryManagementComponent implements OnInit {
  private categoryService = inject(CategoryService);
  private notificationService = inject(NotificationService);

  categories = signal<Category[]>([]);
  isLoading = signal(false);
  showModal = signal(false);
  isEditing = signal(false);
  
  currentCategory = signal<Category>({
    name: ''
  });

  ngOnInit(): void {
    this.loadCategories();
  }

  /**
   * Carga todas las categorías desde el backend
   */
  loadCategories(): void {
    this.isLoading.set(true);
    
    this.categoryService.getAll().subscribe({
      next: (categories) => {
        this.categories.set(categories);
        this.isLoading.set(false);
      },
      error: (error) => {
        console.error('Error al cargar categorías:', error);
        this.notificationService.error('Error al cargar las categorías');
        this.isLoading.set(false);
      }
    });
  }

  /**
   * Abre el modal para crear una nueva categoría
   */
  openCreateModal(): void {
    this.currentCategory.set({ name: '' });
    this.isEditing.set(false);
    this.showModal.set(true);
  }

  /**
   * Abre el modal para editar una categoría existente
   */
  openEditModal(category: Category): void {
    this.currentCategory.set({ ...category });
    this.isEditing.set(true);
    this.showModal.set(true);
  }

  /**
   * Cierra el modal y resetea el formulario
   */
  closeModal(): void {
    this.showModal.set(false);
    this.currentCategory.set({ name: '' });
    this.isEditing.set(false);
  }

  /**
   * Guarda una categoría (crear o actualizar)
   */
  saveCategory(): void {
    const category = this.currentCategory();
    
    if (!category.name.trim()) {
      this.notificationService.warning('El nombre de la categoría es requerido');
      return;
    }

    if (category.name.length > 20) {
      this.notificationService.warning('El nombre no puede exceder 20 caracteres');
      return;
    }

    this.isLoading.set(true);

    const operation = this.isEditing()
      ? this.categoryService.update(category)
      : this.categoryService.create(category);

    operation.subscribe({
      next: (savedCategory) => {
        const message = this.isEditing() 
          ? `Categoría "${savedCategory.name}" actualizada exitosamente`
          : `Categoría "${savedCategory.name}" creada exitosamente`;
        
        this.notificationService.success(message);
        this.closeModal();
        this.loadCategories();
      },
      error: (error) => {
        console.error('Error al guardar categoría:', error);
        const action = this.isEditing() ? 'actualizar' : 'crear';
        this.notificationService.error(`Error al ${action} la categoría`);
        this.isLoading.set(false);
      }
    });
  }

  /**
   * Elimina una categoría
   */
  deleteCategory(category: Category): void {
    if (!category.categoryId) {
      this.notificationService.error('ID de categoría no válido');
      return;
    }

    const confirmMessage = `¿Está seguro de eliminar la categoría "${category.name}"?\n\nEsta acción eliminará la categoría y afectará todos los productos asociados.`;
    
    if (!confirm(confirmMessage)) {
      return;
    }

    this.isLoading.set(true);

    this.categoryService.delete(category.categoryId).subscribe({
      next: () => {
        this.notificationService.success(`Categoría "${category.name}" eliminada exitosamente`);
        this.loadCategories();
      },
      error: (error) => {
        console.error('Error al eliminar categoría:', error);
        
        if (error.status === 409 || error.status === 500) {
          this.notificationService.error('No se puede eliminar la categoría porque tiene productos asociados');
        } else {
          this.notificationService.error('Error al eliminar la categoría');
        }
        
        this.isLoading.set(false);
      }
    });
  }

  /**
   * Actualiza el nombre de la categoría en el formulario
   */
  updateCategoryName(name: string): void {
    this.currentCategory.update(category => ({ ...category, name }));
  }

  /**
   * TrackBy function para optimizar el renderizado de la tabla
   */
  trackByCategoryId(index: number, category: Category): number | undefined {
    return category.categoryId;
  }

  /**
   * Obtiene el ícono según el nombre de la categoría
   */
  getCategoryIcon(name: string): string {
    const categoryName = name.toLowerCase();
    
    // Categorías comunes de restaurante
    if (categoryName.includes('bebida') || categoryName.includes('drink')) return '🥤';
    if (categoryName.includes('comida') || categoryName.includes('food')) return '🍽️';
    if (categoryName.includes('entrada') || categoryName.includes('appetizer')) return '🥗';
    if (categoryName.includes('plato fuerte') || categoryName.includes('main')) return '🍖';
    if (categoryName.includes('postre') || categoryName.includes('dessert')) return '🍰';
    if (categoryName.includes('pizza')) return '🍕';
    if (categoryName.includes('hamburguesa') || categoryName.includes('burger')) return '🍔';
    if (categoryName.includes('pasta')) return '🍝';
    if (categoryName.includes('sushi')) return '🍣';
    if (categoryName.includes('taco') || categoryName.includes('mexican')) return '🌮';
    if (categoryName.includes('ensalada') || categoryName.includes('salad')) return '🥗';
    if (categoryName.includes('sopa') || categoryName.includes('soup')) return '🍲';
    if (categoryName.includes('café') || categoryName.includes('coffee')) return '☕';
    if (categoryName.includes('cerveza') || categoryName.includes('beer')) return '🍺';
    if (categoryName.includes('vino') || categoryName.includes('wine')) return '🍷';
    if (categoryName.includes('cóctel') || categoryName.includes('cocktail')) return '🍹';
    if (categoryName.includes('helado') || categoryName.includes('ice cream')) return '🍦';
    
    return '📦'; // Ícono por defecto
  }

  /**
   * Obtiene la clase CSS según el índice (para variedad de colores)
   */
  getCategoryClass(index: number): string {
    const classes = ['cat-1', 'cat-2', 'cat-3', 'cat-4', 'cat-5', 'cat-6'];
    return classes[index % classes.length];
  }
}



