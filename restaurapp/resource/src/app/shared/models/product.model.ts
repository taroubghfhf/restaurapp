import { Category } from './category.model';

export interface Product {
  productId?: number;
  name: string;
  category: Category;
  price: number;
  stock: number;
  status: boolean;
}

