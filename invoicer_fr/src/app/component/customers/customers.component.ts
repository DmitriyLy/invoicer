import {Component, OnInit} from '@angular/core';
import {BehaviorSubject, catchError, map, Observable, of, startWith} from "rxjs";
import {State} from "../../interface/state";
import {CustomHttpResponse, Home} from "../../interface/appstates";
import {UserService} from "../../service/user.service";
import {CustomerService} from "../../service/customer.service";
import {Customer} from "../../interface/customer";
import { DataState } from 'src/app/enum/datastate.enum';
import {NgForm} from "@angular/forms";
import {Router} from "@angular/router";

@Component({
  selector: 'app-customers',
  templateUrl: './customers.component.html',
  styleUrls: ['./customers.component.css']
})
export class CustomersComponent implements OnInit {
  customersState$: Observable<State<CustomHttpResponse<Home>>>;
  private dataSubject = new BehaviorSubject<CustomHttpResponse<Home>>(null);
  private isLoadingSubject = new BehaviorSubject<boolean>(false);
  private currentPageSubject = new BehaviorSubject<number>(0);
  currentPage$ = this.currentPageSubject.asObservable();
  isLoading$ = this.isLoadingSubject.asObservable();
  readonly DataState = DataState;

  constructor(private router: Router, private customerService: CustomerService) {
  }

  ngOnInit(): void {
    this.customersState$ = this.customerService.searchCustomers$()
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

  goToPage(pageNumber?: number, name?: string): void {
    this.customersState$ = this.customerService.searchCustomers$(name, pageNumber)
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

  goToNextOrPreviousPage(direction?: string, name?: string): void {
    this.goToPage(direction == 'forward' ? this.currentPageSubject.value + 1 : this.currentPageSubject.value - 1, name);
  }

  searchCustomers(searchForm: NgForm): void {
    this.currentPageSubject.next(0);
    this.customersState$ = this.customerService.searchCustomers$(searchForm.value.name)
      .pipe(
        map(response => {
          this.dataSubject.next(response);
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

  selectCustomer(customer: Customer): void {
    this.router.navigate([`/customers/${customer.id}`])
  }

}
