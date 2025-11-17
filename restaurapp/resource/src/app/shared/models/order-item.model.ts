import { OrderTicket } from './order-ticket.model';
import { Product } from './product.model';

export interface OrderItem {
  orderItemId?: number;
  orderTicket: OrderTicket;
  product: Product;
  quantity: number;
  subtotal: number;
}

