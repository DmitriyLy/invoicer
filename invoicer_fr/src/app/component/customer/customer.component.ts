import {Component, OnInit} from '@angular/core';
import {BehaviorSubject, catchError, map, Observable, of, startWith, switchMap} from "rxjs";
import {State} from "../../interface/state";
import {CustomerState, CustomHttpResponse, Home} from "../../interface/appstates";
import {CustomerService} from "../../service/customer.service";
import {DataState} from 'src/app/enum/datastate.enum';
import {ActivatedRoute, ParamMap} from "@angular/router";
import {NgForm} from "@angular/forms";

@Component({
  selector: 'app-customer',
  templateUrl: './customer.component.html',
  styleUrls: ['./customer.component.css']
})
export class CustomerComponent implements OnInit {

  customerState$: Observable<State<CustomHttpResponse<CustomerState>>>;
  private dataSubject = new BehaviorSubject<CustomHttpResponse<CustomerState>>(null);
  private isLoadingSubject = new BehaviorSubject<boolean>(false);
  isLoading$ = this.isLoadingSubject.asObservable();
  readonly DataState = DataState;
  private readonly CUSTOMER_ID: string = 'id';

  constructor(private customerService: CustomerService, private activatedRoute: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.customerState$ = this.activatedRoute.paramMap.pipe(
      switchMap((params: ParamMap) => {
        return this.customerService.customer$(+params.get(this.CUSTOMER_ID))
          .pipe(
            map(response => {
              this.dataSubject.next(response);
              return {dataState: DataState.LOADED, appData: response};
            }),
            startWith({dataState: DataState.LOADING}),
            catchError((error: string) => {
              return of({dataState: DataState.ERROR, error})
            })
          )
      })
    );
  }

  updateCustomer(customerForm: NgForm): void {
    this.isLoadingSubject.next(true);
    this.customerState$ = this.customerService.update$(customerForm.value)
      .pipe(
        map(response => {
          this.dataSubject.next({...response,
            data: {
              ...response.data,
              customer: {...response.data.customer, invoices: this.dataSubject.value.data.customer.invoices}
            }
          });
          this.isLoadingSubject.next(false);
          return {dataState: DataState.LOADED, appData: this.dataSubject.value};
        }),
        startWith({dataState: DataState.LOADING}),
        catchError((error: string) => {
          this.isLoadingSubject.next(false);
          return of({dataState: DataState.ERROR, error})
        })
      )
  }

}
