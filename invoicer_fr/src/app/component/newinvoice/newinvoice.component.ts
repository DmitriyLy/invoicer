import {Component, OnInit} from '@angular/core';
import {BehaviorSubject, catchError, map, Observable, of, startWith} from "rxjs";
import {State} from "../../interface/state";
import {CustomHttpResponse, UserCustomers} from "../../interface/appstates";
import {NgForm} from "@angular/forms";
import {DataState} from 'src/app/enum/datastate.enum';
import {InvoiceService} from "../../service/invoice.service";

@Component({
  selector: 'app-newinvoice',
  templateUrl: './newinvoice.component.html',
  styleUrls: ['./newinvoice.component.css']
})
export class NewinvoiceComponent implements OnInit {
  newInvoiceState$: Observable<State<CustomHttpResponse<UserCustomers>>>;
  private dataSubject = new BehaviorSubject<CustomHttpResponse<UserCustomers>>(null);
  private isLoadingSubject = new BehaviorSubject<boolean>(false);
  isLoading$ = this.isLoadingSubject.asObservable();
  readonly DataState = DataState;

  constructor(private invoiceService: InvoiceService) {
  }

  ngOnInit(): void {
    this.newInvoiceState$ = this.invoiceService.newInvoice$()
      .pipe(
        map(response => {
          this.dataSubject.next(response);
          return {
            dataState: DataState.LOADED,
            appData: response
          };
        }),
        startWith({dataState: DataState.LOADING}),
        catchError((error: string) => {
          return of({dataState: DataState.ERROR, appData: this.dataSubject.value, error})
        })
      )
  }

  newInvoice(newInvoiceForm: NgForm): void {
    this.isLoadingSubject.next(true);
    this.newInvoiceState$ = this.invoiceService.createInvoice$(newInvoiceForm.value.customerId, newInvoiceForm.value)
      .pipe(
        map(response => {
          newInvoiceForm.reset({ status: 'PENDING'});
          this.isLoadingSubject.next(false);
          this.dataSubject.next({...this.dataSubject.value, message: response.message});
          return {
            dataState: DataState.LOADED,
            appData: this.dataSubject.value
          };
        }),
        startWith({dataState: DataState.LOADED, appData: this.dataSubject.value}),
        catchError((error: string) => {
          this.isLoadingSubject.next(false);
          return of({dataState: DataState.LOADED, appData: this.dataSubject.value, error});
        })
      )
  }
}
