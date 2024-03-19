import {Injectable} from "@angular/core";
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {catchError, Observable, tap, throwError} from "rxjs";
import {CustomerState, CustomHttpResponse, Home, Invoices, UserCustomers} from "../interface/appstates";
import {Invoice} from "../interface/invoice";

@Injectable({
  providedIn: 'root'
})
export class InvoiceService {
  private readonly server: string = 'http://localhost:8080';

  constructor(private http: HttpClient) {
  }

  newInvoice$ = () => <Observable<CustomHttpResponse<CustomerState>>>
    this.http.get<CustomHttpResponse<UserCustomers>>
    (`${this.server}/api/v1/invoice/new`)
      .pipe(
        tap(console.log),
        catchError(this.handleError)
      );

  createInvoice$ = (customerId: number, invoice: Invoice) => <Observable<CustomHttpResponse<CustomerState>>>
    this.http.post<CustomHttpResponse<UserCustomers>>
    (`${this.server}/api/v1/customer/add/invoice/to/customer/${customerId}`, invoice)
      .pipe(
        tap(console.log),
        catchError(this.handleError)
      );

  invoices = (page: number = 0, size: number = 10) => <Observable<CustomHttpResponse<Invoices>>>
    this.http.get<CustomHttpResponse<Invoices>>
    (`${this.server}/api/v1/invoice/list?page=${page}&size=${size}`)
      .pipe(
        tap(console.log),
        catchError(this.handleError)
      );

  private handleError(error: HttpErrorResponse): Observable<never> {
    console.log(error);
    let errorMessage: string;
    if (error.error instanceof ErrorEvent) {
      errorMessage = `A client error occurred - ${error.error.message}`;
    } else {
      if (error.error.reason) {
        errorMessage = error.error.reason;
        console.log(errorMessage);
      } else {
        errorMessage = `An error occurred - Error status ${error.status}`;
      }
    }
    return throwError(() => errorMessage);
  }

}
