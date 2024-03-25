import {Component, OnInit} from '@angular/core';
import {BehaviorSubject, catchError, map, Observable, of, startWith, switchMap} from "rxjs";
import {State} from "../../interface/state";
import {CustomerState, CustomHttpResponse} from "../../interface/appstates";
import {ActivatedRoute, ParamMap} from "@angular/router";
import {DataState} from 'src/app/enum/datastate.enum';
import {InvoiceService} from "../../service/invoice.service";
import { jsPDF } from 'jspdf';

@Component({
  selector: 'app-invoice',
  templateUrl: './invoice.component.html',
  styleUrls: ['./invoice.component.css']
})
export class InvoiceComponent implements OnInit {

  invoiceState$: Observable<State<CustomHttpResponse<CustomerState>>>;
  private dataSubject = new BehaviorSubject<CustomHttpResponse<CustomerState>>(null);
  private isLoadingSubject = new BehaviorSubject<boolean>(false);
  isLoading$ = this.isLoadingSubject.asObservable();
  readonly DataState = DataState;
  private readonly INVOICE_ID: string = 'id';

  constructor(private invoiceService: InvoiceService, private activatedRoute: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.invoiceState$ = this.activatedRoute.paramMap.pipe(
      switchMap((params: ParamMap) => {
        return this.invoiceService.invoice(+params.get(this.INVOICE_ID))
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

  exportAsPDF(): void {
    const  filename = `invoice-${this.dataSubject.value.data['invoice'].number}.pdf`;
    const doc = new jsPDF();
    doc.html(
      document.getElementById('invoice'),
      {margin: 5, windowWidth: 1000, width: 200, callback: (invoice) => invoice.save(filename)}
    );
  }

}
