import {Injectable} from "@angular/core";
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpResponse} from "@angular/common/http";
import {Observable, of, tap} from "rxjs";
import {HttpCacheService} from "../service/http.cache.service";
import {AbstractHttpInterceptor} from "./abstract.http.interceptor";

@Injectable({providedIn: 'root'})
export class ResponseCacheInterceptor extends AbstractHttpInterceptor implements HttpInterceptor {

  constructor(private httpCacheService: HttpCacheService) {
    super();
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<unknown>> | Observable<HttpResponse<unknown>> {
    if (this.isWhiteListUrl(request.url)) {
      return next.handle(request);
    }

    if (request.method != 'GET' || request.url.includes('download')) {
      this.httpCacheService.evictAll();
      return next.handle(request);
    }

    const cachedResponse: HttpResponse<any> = this.httpCacheService.get(request.url);

    if (cachedResponse) {
      this.httpCacheService.logCache();
      return of(cachedResponse);
    }

    return this.handleRequestCache(request, next);
  }

  private handleRequestCache(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<unknown>> | Observable<HttpResponse<unknown>>  {
    return next.handle(request).pipe(
      tap(response => {
        if ( response instanceof  HttpResponse) {
          this.httpCacheService.put(request.url, response);
        }
      })
    );
  }
}
