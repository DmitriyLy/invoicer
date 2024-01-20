import {Component} from '@angular/core';
import {Router} from "@angular/router";
import {UserService} from "../../service/user.service";
import {NgForm} from "@angular/forms";
import {BehaviorSubject, catchError, map, Observable, of, startWith} from "rxjs";
import {LoginState} from "../../interface/appstates";
import {DataState} from "../../enum/datastate.enum";

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  loginState$: Observable<LoginState> = of({dataState: DataState.LOADED});
  private phoneSubject = new BehaviorSubject<string | null>(null);
  private emailSubject = new BehaviorSubject<string | null>(null);
  readonly DataState = DataState;

  constructor(private router: Router, private userService: UserService) {
  }

  login(loginForm: NgForm): void {
    this.loginState$ = this.userService.login$(loginForm.value.email, loginForm.value.password)
      .pipe(
        map(response => {
          if (response.data != null && response.data.user != null && response.data.user.isUsingMfa) {
            this.phoneSubject.next(response.data.user.phone != null ? response.data.user.phone : null);
            this.emailSubject.next(response.data.user.email);
            return {
              dataState: DataState.LOADED, isUsingMfa: true, loginSuccess: false,
              phone: response.data.user.phone != null ? response.data.user.phone.substring(response.data.user.phone.length - 4) : null
            };
          } else {
            //localStorage.setItem(Key.TOKEN, response.data?.access_token);
            //localStorage.setItem(Key.REFRESH_TOKEN, response.data?.refresh_token);
            this.router.navigate(['/']);
            return { dataState: DataState.LOADED, loginSuccess: true };
          }
        }),
        startWith({ dataState: DataState.LOADING, isUsingMfa: false }),
        catchError((error: string) => {
          return of({ dataState: DataState.ERROR, isUsingMfa: false, loginSuccess: false, error })
        })
      )
  }

}
