import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_CONFIG } from '../shared/constants/api.constants';
import { Table } from '../shared/models';

@Injectable({
  providedIn: 'root'
})
export class TableService {
  private http = inject(HttpClient);
  private apiUrl = `${API_CONFIG.baseUrl}${API_CONFIG.endpoints.table}`;

  /**
   * Get all tables
   * GET /table
   */
  getAll(): Observable<Table[]> {
    return this.http.get<Table[]>(this.apiUrl);
  }

  /**
   * Create a new table
   * POST /table
   */
  create(table: Table): Observable<Table> {
    return this.http.post<Table>(this.apiUrl, table);
  }

  /**
   * Update an existing table
   * PUT /table
   */
  update(table: Table): Observable<Table> {
    return this.http.put<Table>(this.apiUrl, table);
  }

  /**
   * Delete a table
   * DELETE /table
   */
  delete(tableId: number): Observable<void> {
    return this.http.delete<void>(this.apiUrl, {
      body: { tableId }
    });
  }
}


