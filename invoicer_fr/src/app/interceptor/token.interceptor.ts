import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor, HttpResponse, HttpErrorResponse
} from '@angular/common/http';
import {BehaviorSubject, catchError, Observable, switchMap, throwError} from 'rxjs';
import {Key} from "../enum/key.enum";
import {UserService} from "../service/user.service";
import {CustomHttpResponse, Profile} from "../interface/appstates";
import {AbstractHttpInterceptor} from "./abstract.http.interceptor";

@Injectable({ providedIn: 'root' })
export class TokenInterceptor extends AbstractHttpInterceptor implements HttpInterceptor {
  private isTokenRefreshing: boolean = false;
  private refreshTokenSubject: BehaviorSubject<CustomHttpResponse<Profile>> = new BehaviorSubject<CustomHttpResponse<Profile>>(null);

  constructor(private userService: UserService) {
    super();
  }

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> | Observable<HttpResponse<unknown>> {
    if (this.isWhiteListUrl(request.url)) {
      return next.handle(request);
    }

    return next.handle(this.addAuthorizationTokenHeader(request, localStorage.getItem(Key.TOKEN)))
      .pipe(
        catchError((error: HttpErrorResponse) => {
          if (error instanceof HttpErrorResponse && error.status === 401 && error.error.reason.includes('expired')) {
            return this.handleRefreshToken(request, next);
          } else {
            return throwError(() => error);
          }
        })
      );
  }

  private addAuthorizationTokenHeader(request: HttpRequest<unknown>, token: string): HttpRequest<unknown> {
    return request.clone(
      { setHeaders: {
          Authorization: `Bearer ${token}`
        }}
    );
  }

  private handleRefreshToken(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    if (!this.isTokenRefreshing) {
      console.log("Refreshing token......");
      this.isTokenRefreshing = true;
      this.refreshTokenSubject.next(null);
      return this.userService.refreshToken$()
        .pipe(
          switchMap((response) => {
            console.log('Token refresh response: ', response);
            this.isTokenRefreshing = false;
            this.refreshTokenSubject.next(response);
            console.log('Sending original request: ', request);
            console.log('New Token: ', response.data.access_token);
            return next.handle(this.addAuthorizationTokenHeader(request, response.data.access_token));

          })
        );
    } else {
      this.refreshTokenSubject.pipe(
        switchMap((response) => {
          return next.handle(this.addAuthorizationTokenHeader(request, response.data.access_token));
        })
      );
    }
    throw new Error();
  }
}
