import { User } from './user.model';
import { Table } from './table.model';
import { Status } from './status.model';

export interface OrderTicket {
  orderTicketId?: number;
  date: string;
  table: Table;
  waiter: User;
  chef: User;
  status: Status;
}

