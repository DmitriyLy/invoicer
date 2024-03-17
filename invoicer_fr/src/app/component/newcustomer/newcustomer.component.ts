import {Component, OnInit} from '@angular/core';
import {BehaviorSubject, catchError, map, Observable, of, startWith} from "rxjs";
import {State} from "../../interface/state";
import {CustomHttpResponse, Home} from "../../interface/appstates";
import {CustomerService} from "../../service/customer.service";
import { DataState } from 'src/app/enum/datastate.enum';
import {NgForm} from "@angular/forms";

@Component({
  selector: 'app-newcustomer',
  templateUrl: './newcustomer.component.html',
  styleUrls: ['./newcustomer.component.css']
})
export class NewcustomerComponent implements OnInit {
  newCustomerState$: Observable<State<CustomHttpResponse<Home>>>;
  private dataSubject = new BehaviorSubject<CustomHttpResponse<Home>>(null);
  private isLoadingSubject = new BehaviorSubject<boolean>(false);
  isLoading$ = this.isLoadingSubject.asObservable();
  readonly DataState = DataState;

  constructor(private customerService: CustomerService) {
  }

  ngOnInit(): void {
    this.newCustomerState$ = this.customerService.customers$()
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

  createCustomer(newCustomerForm: NgForm): void {
    this.isLoadingSubject.next(true);
    this.newCustomerState$ = this.customerService.newCustomer$(newCustomerForm.value)
      .pipe(
        map(response => {
          console.log(response);
          newCustomerForm.reset({ type: 'INDIVIDUAL',  status: 'ACTIVE'});
          this.isLoadingSubject.next(false);
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
