import { Pipe, PipeTransform } from '@angular/core';
import {Invoice} from "../interface/invoice";

@Pipe({
  name: 'GetTotals'
})
export class GetTotals implements PipeTransform {

  transform(value: any, args: string): any {
    if (args === 'invoice_totals') {
      let total: number = 0;
      let invoices: Invoice[] = value;
      invoices.forEach(invoice => {
        total += invoice.total;
      });
      return total.toFixed(2);
    }

    throw new Error("Unknown ars value");
  }

}
