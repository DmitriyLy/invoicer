import {Invoice} from "./invoice";

export interface Customer {
  id: number;
  name: string;
  email: string;
  type: string;
  status: string;
  address: string;
  phone: string;
  imageUrl: string;
  createdAt: Date;
  invoices?: Invoice[];
}
