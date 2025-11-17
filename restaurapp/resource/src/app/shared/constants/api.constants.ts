import { environment } from '../../../environments/environment';

export const API_CONFIG = {
  baseUrl: environment.apiUrl,
  endpoints: {
    auth: '/auth',
    role: '/role',
    status: '/status',
    user: '/user',
    category: '/category',
    product: '/product',
    table: '/table',
    orderTicket: '/order-ticket',
    orderItem: '/order-item'
  }
};

