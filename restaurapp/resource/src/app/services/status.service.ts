import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_CONFIG } from '../shared/constants/api.constants';
import { Status } from '../shared/models';

@Injectable({
  providedIn: 'root'
})
export class StatusService {
  private http = inject(HttpClient);
  private apiUrl = `${API_CONFIG.baseUrl}${API_CONFIG.endpoints.status}`;

  /**
   * Get all statuses
   * GET /status
   */
  getAll(): Observable<Status[]> {
    return this.http.get<Status[]>(this.apiUrl);
  }

  /**
   * Create a new status
   * POST /status
   */
  create(status: Status): Observable<Status> {
    return this.http.post<Status>(this.apiUrl, status);
  }

  /**
   * Update an existing status
   * PUT /status
   */
  update(status: Status): Observable<Status> {
    return this.http.put<Status>(this.apiUrl, status);
  }

  /**
   * Delete a status
   * DELETE /status
   */
  delete(statusId: number): Observable<void> {
    return this.http.delete<void>(this.apiUrl, {
      body: { statusId }
    });
  }
}



