import { Role } from './role.model';

export interface User {
  userId?: number;
  name: string;
  email: string;
  password?: string;
  role: Role;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  email: string;
  name: string;
  role: string;
  userId: number;
}

