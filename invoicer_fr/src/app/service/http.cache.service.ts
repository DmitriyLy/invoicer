import {Injectable} from "@angular/core";
import {HttpResponse} from "@angular/common/http";

@Injectable({ providedIn: 'root' })
export class HttpCacheService {
  private cache: { [key: string]: HttpResponse<any> } = {};

  put = (key: string, httpResponse: HttpResponse<any>): void => {
    console.log('Caching response', httpResponse);
    this.cache[key] = httpResponse;
  }

  get = (key: string): HttpResponse<any> | null | undefined => this.cache[key];

  evict = (key: string): boolean => delete this.cache[key];

  evictAll = (): void => {
    console.log('Clearing cache');
    this.cache = {};
  };

  logCache = (): void => console.log(this.cache);

}
