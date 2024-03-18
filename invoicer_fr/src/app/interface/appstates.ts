import { DataState } from "../enum/datastate.enum";
import { User } from "./user";
import {UserEvent} from "./user-event";
import {Role} from "./role";
import {Customer} from "./customer";
import {Stats} from "./stats";

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

export interface Page {
  content: Customer[];
  totalPages: number;
  totalElements: number;
  numberOfElements: number;
  size: number;
  number: number;
}

export interface Home {
  page?: Page;
  user?: User;
  stats?: Stats;
}

export  interface  CustomerState {
  user: User;
  customer: Customer
}
