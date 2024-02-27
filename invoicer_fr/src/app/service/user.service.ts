import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpHeaders} from '@angular/common/http';
import {catchError, Observable, tap, throwError} from 'rxjs';
import {CustomHttpResponse, Profile} from '../interface/appstates';
import {User} from "../interface/user";
import {Key} from "../enum/key.enum";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private readonly server: string = 'http://localhost:8080';
  private readonly token: string = '';

  constructor(private http: HttpClient) {
  }

  login$ = (email: string, password: string) => <Observable<CustomHttpResponse<Profile>>>
    this.http.post<CustomHttpResponse<Profile>>
    (`${this.server}/api/v1/user/login`, {email, password})
      .pipe(
        tap(console.log),
        catchError(this.handleError)
      );

  verifyCode$ = (email: string, code: string) => <Observable<CustomHttpResponse<Profile>>>
    this.http.get<CustomHttpResponse<Profile>>
    (`${this.server}/api/v1/user/verify/${email}/${code}`)
      .pipe(
        tap(console.log),
        catchError(this.handleError)
      );

  profile$ = () => <Observable<CustomHttpResponse<Profile>>>
    this.http.get<CustomHttpResponse<Profile>>
    (`${this.server}/api/v1/user/profile`, {headers: {'Authorization': this.token}})
      .pipe(
        tap(console.log),
        catchError(this.handleError)
      );

  update$ = (user: User) => <Observable<CustomHttpResponse<Profile>>>
    this.http.patch<CustomHttpResponse<Profile>>
    (`${this.server}/api/v1/user/update`, user, {headers: {'Authorization': this.token}})
      .pipe(
        tap(console.log),
        catchError(this.handleError)
      );

  refreshToken$ = () => <Observable<CustomHttpResponse<Profile>>>
    this.http.post<CustomHttpResponse<Profile>>
    (`${this.server}/api/v1/user/refresh/token`, null, {headers: {'Authorization': `Bearer ${localStorage.getItem(Key.REFRESH_TOKEN)}`}})
      .pipe(
        tap(response => {
          console.log(response);
          localStorage.removeItem(Key.TOKEN);
          localStorage.removeItem(Key.REFRESH_TOKEN);
          localStorage.setItem(Key.TOKEN, response.data.access_token);
          localStorage.setItem(Key.REFRESH_TOKEN, response.data.refresh_token);
        }),
        catchError(this.handleError)
      );

  updatePassword$ = (form: {
    currentPassword: string,
    newPassword: string,
    confirmPassword: string
  }) => <Observable<CustomHttpResponse<Profile>>>
    this.http.patch<CustomHttpResponse<Profile>>
    (`${this.server}/api/v1/user/update/password`, form)
      .pipe(
        tap(console.log),
        catchError(this.handleError)
      );

  updateUserRole$ = (roleName: string) => <Observable<CustomHttpResponse<Profile>>>
    this.http.patch<CustomHttpResponse<Profile>>
    (`${this.server}/api/v1/user/update/role/${roleName}`, {})
      .pipe(
        tap(console.log),
        catchError(this.handleError)
      );

  updateAccountSettings$ = (form: { enabled: boolean, notLocked: boolean }) => <Observable<CustomHttpResponse<Profile>>>
    this.http.patch<CustomHttpResponse<Profile>>
    (`${this.server}/api/v1/user/update/account/settings`, form)
      .pipe(
        tap(console.log),
        catchError(this.handleError)
      );

  toggleMfa$ = () => <Observable<CustomHttpResponse<Profile>>>
    this.http.patch<CustomHttpResponse<Profile>>
    (`${this.server}/api/v1/user/toggle/mfa`, {})
      .pipe(
        tap(console.log),
        catchError(this.handleError)
      );

  private handleError(error: HttpErrorResponse): Observable<never> {
    console.log(error);
    let errorMessage: string;
    if (error.error instanceof ErrorEvent) {
      errorMessage = `A client error occurred - ${error.error.message}`;
    } else {
      if (error.error.reason) {
        errorMessage = error.error.reason;
        console.log(errorMessage);
      } else {
        errorMessage = `An error occurred - Error status ${error.status}`;
      }
    }
    return throwError(() => errorMessage);
  }
}
