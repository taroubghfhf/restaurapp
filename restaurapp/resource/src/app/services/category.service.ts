import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_CONFIG } from '../shared/constants/api.constants';
import { Category } from '../shared/models';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {
  private http = inject(HttpClient);
  private apiUrl = `${API_CONFIG.baseUrl}${API_CONFIG.endpoints.category}`;

  /**
   * Get all categories
   * GET /category
   */
  getAll(): Observable<Category[]> {
    return this.http.get<Category[]>(this.apiUrl);
  }

  /**
   * Create a new category
   * POST /category
   */
  create(category: Category): Observable<Category> {
    return this.http.post<Category>(this.apiUrl, category);
  }

  /**
   * Update an existing category
   * PUT /category
   */
  update(category: Category): Observable<Category> {
    return this.http.put<Category>(this.apiUrl, category);
  }

  /**
   * Delete a category
   * DELETE /category
   */
  delete(categoryId: number): Observable<void> {
    return this.http.delete<void>(this.apiUrl, {
      body: { categoryId }
    });
  }
}


