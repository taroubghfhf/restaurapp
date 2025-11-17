import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_CONFIG } from '../shared/constants/api.constants';
import { OrderTicket, OrderItem } from '../shared/models';

@Injectable({
  providedIn: 'root'
})
export class OrderTicketService {
  private http = inject(HttpClient);
  private apiUrl = `${API_CONFIG.baseUrl}${API_CONFIG.endpoints.orderTicket}`;

  /**
   * Get all order tickets
   * GET /order-ticket
   */
  getAll(): Observable<OrderTicket[]> {
    return this.http.get<OrderTicket[]>(this.apiUrl);
  }

  /**
   * Create a new order ticket
   * POST /order-ticket
   */
  create(orderTicket: OrderTicket): Observable<OrderTicket> {
    return this.http.post<OrderTicket>(this.apiUrl, orderTicket);
  }

  /**
   * Update an existing order ticket (full update)
   * PUT /order-ticket
   */
  update(orderTicket: OrderTicket): Observable<OrderTicket> {
    return this.http.put<OrderTicket>(this.apiUrl, orderTicket);
  }

  /**
   * Update order ticket status
   * PATCH /order-ticket/{orderTicketId}/status/{statusId}
   */
  updateStatus(orderTicketId: number, statusId: number): Observable<OrderTicket> {
    return this.http.patch<OrderTicket>(
      `${this.apiUrl}/${orderTicketId}/status/${statusId}`,
      null
    );
  }

  /**
   * Get order tickets by status
   * GET /order-ticket/status/{statusId}
   */
  getByStatus(statusId: number): Observable<OrderTicket[]> {
    return this.http.get<OrderTicket[]>(`${this.apiUrl}/status/${statusId}`);
  }

  /**
   * Get order tickets by waiter
   * GET /order-ticket/waiter/{waiterId}
   */
  getByWaiter(waiterId: number): Observable<OrderTicket[]> {
    return this.http.get<OrderTicket[]>(`${this.apiUrl}/waiter/${waiterId}`);
  }

  /**
   * Get order tickets by chef
   * GET /order-ticket/chef/{chefId}
   */
  getByChef(chefId: number): Observable<OrderTicket[]> {
    return this.http.get<OrderTicket[]>(`${this.apiUrl}/chef/${chefId}`);
  }

  /**
   * Get order items for a specific order ticket
   * GET /order-ticket/{orderTicketId}/items
   */
  getOrderItems(orderTicketId: number): Observable<OrderItem[]> {
    return this.http.get<OrderItem[]>(`${this.apiUrl}/${orderTicketId}/items`);
  }

  /**
   * Delete an order ticket
   * DELETE /order-ticket
   */
  delete(orderTicketId: number): Observable<void> {
    return this.http.delete<void>(this.apiUrl, {
      body: { orderTicketId }
    });
  }
}

