import {Component, OnInit} from '@angular/core';
import {BehaviorSubject, catchError, map, Observable, of, startWith} from "rxjs";
import {State} from "../../interface/state";
import {CustomHttpResponse, Invoices} from "../../interface/appstates";
import {Router} from "@angular/router";
import {DataState} from 'src/app/enum/datastate.enum';
import {InvoiceService} from "../../service/invoice.service";

@Component({
  selector: 'app-invoices',
  templateUrl: './invoices.component.html',
  styleUrls: ['./invoices.component.css']
})
export class InvoicesComponent implements OnInit {
  invoicesState$: Observable<State<CustomHttpResponse<Invoices>>>;
  private dataSubject = new BehaviorSubject<CustomHttpResponse<Invoices>>(null);
  private isLoadingSubject = new BehaviorSubject<boolean>(false);
  private currentPageSubject = new BehaviorSubject<number>(0);
  currentPage$ = this.currentPageSubject.asObservable();
  isLoading$ = this.isLoadingSubject.asObservable();
  readonly DataState = DataState;

  constructor(private router: Router, private invoiceService: InvoiceService) {
  }

  ngOnInit(): void {
    this.invoicesState$ = this.invoiceService.invoices()
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

  goToPage(pageNumber?: number): void {
    this.invoicesState$ = this.invoiceService.invoices(pageNumber)
      .pipe(
        map(response => {
          this.dataSubject.next(response);
          this.currentPageSubject.next(pageNumber);
          return {
            dataState: DataState.LOADED,
            appData: response
          };
        }),
        startWith({dataState: DataState.LOADED, appData: this.dataSubject.value}),
        catchError((error: string) => {
          return of({dataState: DataState.ERROR, appData: this.dataSubject.value, error})
        })
      )
  }

  goToNextOrPreviousPage(direction?: string): void {
    this.goToPage(direction == 'forward' ? this.currentPageSubject.value + 1 : this.currentPageSubject.value - 1);
  }

}
