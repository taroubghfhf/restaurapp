import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_CONFIG } from '../shared/constants/api.constants';
import { Role } from '../shared/models';

@Injectable({
  providedIn: 'root'
})
export class RoleService {
  private http = inject(HttpClient);
  private apiUrl = `${API_CONFIG.baseUrl}${API_CONFIG.endpoints.role}`;

  /**
   * Get all roles
   * GET /role
   */
  getAll(): Observable<Role[]> {
    return this.http.get<Role[]>(this.apiUrl);
  }

  /**
   * Create a new role
   * POST /role
   */
  create(role: Role): Observable<Role> {
    return this.http.post<Role>(this.apiUrl, role);
  }

  /**
   * Update an existing role
   * PUT /role
   */
  update(role: Role): Observable<Role> {
    return this.http.put<Role>(this.apiUrl, role);
  }

  /**
   * Delete a role
   * DELETE /role
   */
  delete(roleId: number): Observable<void> {
    return this.http.delete<void>(this.apiUrl, {
      body: { roleId }
    });
  }
}





