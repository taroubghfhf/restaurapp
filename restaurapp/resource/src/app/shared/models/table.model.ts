import { Status } from './status.model';

export interface Table {
  tableId?: number;
  capacity: number;
  location: number;
  status: Status;
}

