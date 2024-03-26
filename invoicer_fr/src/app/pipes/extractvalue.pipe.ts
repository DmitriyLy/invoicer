import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'ExtractArrayValue'
})
export class ExtractArrayValue implements PipeTransform {

  transform(value: any, args: string): any {
    if (args === 'number') {
      let numberArray: number[] = [];
      for (let i = 0; i < value; i++) {
        numberArray.push(i);
      }
      return numberArray;
    }

    throw new Error("Unknown ars value");
  }

}
