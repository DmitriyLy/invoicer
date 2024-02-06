import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpHeaders} from '@angular/common/http';
import {catchError, Observable, tap, throwError} from 'rxjs';
import {CustomHttpResponse, Profile} from '../interface/appstates';
import {User} from "../interface/user";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private readonly server: string = 'http://localhost:8080';
  private readonly token: string = 'Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJJbnZvaWNlciBhcHAiLCJhdWQiOiIiLCJpYXQiOjE3MDcyNDQ4NzQsInN1YiI6InRlc3RAdGVzdC5jb20iLCJhdXRob3JpdGllcyI6WyJSRUFEOlVTRVIiLCJSRUFEOkNVU1RPTUVSIiwiVVBEQVRFOlVTRVIiLCJVUERBVEU6Q1VTVE9NRVIiLCJDUkVBVEU6VVNFUiIsIkNSRUFURTpDVVNUT01FUiIsIkRFTEVURTpVU0VSIiwiREVMRVRFOkNVU1RPTUVSIl0sImV4cCI6MTczMzE2NDg3NH0.7uaF2IphFRLnULHbeR5_ezoBvW6z2mlaK8H-1FgewS2lxaFDnczrUMkaZLSXNFbIw-o6adH-rTkCXL2Ek95qGQ';

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
    (`${this.server}/api/v1/user/update`, user,{headers: {'Authorization': this.token}})
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
