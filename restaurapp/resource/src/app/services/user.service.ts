import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_CONFIG } from '../shared/constants/api.constants';
import { User } from '../shared/models';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private http = inject(HttpClient);
  private apiUrl = `${API_CONFIG.baseUrl}${API_CONFIG.endpoints.user}`;

  /**
   * Get all users
   * GET /user
   */
  getAll(): Observable<User[]> {
    return this.http.get<User[]>(this.apiUrl);
  }

  /**
   * Create a new user
   * POST /user
   */
  create(user: User): Observable<User> {
    return this.http.post<User>(this.apiUrl, user);
  }

  /**
   * Update an existing user
   * PUT /user
   */
  update(user: User): Observable<User> {
    return this.http.put<User>(this.apiUrl, user);
  }

  /**
   * Delete a user
   * DELETE /user
   */
  delete(userId: number): Observable<void> {
    return this.http.delete<void>(this.apiUrl, {
      body: { userId }
    });
  }
}





