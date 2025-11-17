import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_CONFIG } from '../shared/constants/api.constants';
import { Product } from '../shared/models';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private http = inject(HttpClient);
  private apiUrl = `${API_CONFIG.baseUrl}${API_CONFIG.endpoints.product}`;

  /**
   * Get all products
   * GET /product
   */
  getAll(): Observable<Product[]> {
    return this.http.get<Product[]>(this.apiUrl);
  }

  /**
   * Create a new product
   * POST /product
   */
  create(product: Product): Observable<Product> {
    return this.http.post<Product>(this.apiUrl, product);
  }

  /**
   * Update an existing product
   * PUT /product
   */
  update(product: Product): Observable<Product> {
    return this.http.put<Product>(this.apiUrl, product);
  }

  /**
   * Delete a product
   * DELETE /product
   */
  delete(productId: number): Observable<void> {
    return this.http.delete<void>(this.apiUrl, {
      body: { productId }
    });
  }
}


