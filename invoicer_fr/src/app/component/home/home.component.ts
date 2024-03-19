import {Component, OnInit} from '@angular/core';
import {BehaviorSubject, catchError, map, Observable, of, startWith} from "rxjs";
import {State} from "../../interface/state";
import {CustomHttpResponse, Home, CustomerPage, Profile} from "../../interface/appstates";
import {UserService} from "../../service/user.service";
import {DataState} from 'src/app/enum/datastate.enum';
import {CustomerService} from "../../service/customer.service";
import {User} from "../../interface/user";
import {Customer} from "../../interface/customer";
import {Router} from "@angular/router";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  homeState$: Observable<State<CustomHttpResponse<Home>>>;
  private dataSubject = new BehaviorSubject<CustomHttpResponse<Home>>(null);
  private isLoadingSubject = new BehaviorSubject<boolean>(false);
  private currentPageSubject = new BehaviorSubject<number>(0);
  currentPage$ = this.currentPageSubject.asObservable();
  isLoading$ = this.isLoadingSubject.asObservable();
  readonly DataState = DataState;

  constructor(private router: Router, private customerService: CustomerService) {
  }

  ngOnInit(): void {
    this.homeState$ = this.customerService.customers$()
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
    this.homeState$ = this.customerService.customers$(pageNumber)
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

  selectCustomer(customer: Customer): void {
    this.router.navigate([`/customers/${customer.id}`])
  }

}
