import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_CONFIG } from '../shared/constants/api.constants';
import { OrderItem } from '../shared/models';

@Injectable({
  providedIn: 'root'
})
export class OrderItemService {
  private http = inject(HttpClient);
  private apiUrl = `${API_CONFIG.baseUrl}${API_CONFIG.endpoints.orderItem}`;

  /**
   * Get all order items
   * GET /order-item
   */
  getAll(): Observable<OrderItem[]> {
    return this.http.get<OrderItem[]>(this.apiUrl);
  }

  /**
   * Create a new order item
   * POST /order-item
   */
  create(orderItem: OrderItem): Observable<OrderItem> {
    return this.http.post<OrderItem>(this.apiUrl, orderItem);
  }

  /**
   * Update an existing order item
   * PUT /order-item
   */
  update(orderItem: OrderItem): Observable<OrderItem> {
    return this.http.put<OrderItem>(this.apiUrl, orderItem);
  }

  /**
   * Delete an order item
   * DELETE /order-item
   */
  delete(orderItemId: number): Observable<void> {
    return this.http.delete<void>(this.apiUrl, {
      body: { orderItemId }
    });
  }
}

