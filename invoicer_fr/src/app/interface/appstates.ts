import { DataState } from "../enum/datastate.enum";
import { User } from "./user";
import {UserEvent} from "./user-event";
import {Role} from "./role";
import {Customer} from "./customer";
import {Stats} from "./stats";
import {Invoice} from "./invoice";

export interface LoginState {
    dataState: DataState;
    loginSuccess?: boolean;
    error?: string;
    message?: string;
    isUsingMfa?: boolean;
    phone?: string;
}

export interface CustomHttpResponse<T> {
    timestamp: Date;
    statusCode: number;
    status: string;
    message: string;
    reason?: string;
    developerMessage?: string;
    data?: T;
}

export interface Profile {
    user?: User;
    access_token: string;
    events: UserEvent[];
    roles: Role[];
    refresh_token: string;
}

export interface CustomerPage {
  content: Customer[];
  totalPages: number;
  totalElements: number;
  numberOfElements: number;
  size: number;
  number: number;
}

export interface InvoicePage {
  content: Invoice[];
  totalPages: number;
  totalElements: number;
  numberOfElements: number;
  size: number;
  number: number;
}

export interface Home {
  page?: CustomerPage;
  user?: User;
  stats?: Stats;
}

export interface Invoices {
  page?: InvoicePage;
  user?: User;
}

export interface CustomerState {
  user: User;
  customer: Customer;
  invoice?: Invoice;
}

export interface UserCustomers {
  user?: User;
  customers?: Customer[]
}
