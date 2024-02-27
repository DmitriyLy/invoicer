import {Component, OnInit} from '@angular/core';
import {BehaviorSubject, catchError, map, Observable, of, startWith} from "rxjs";
import {Router} from "@angular/router";
import {UserService} from "../../service/user.service";
import {State} from "../../interface/state";
import {DataState} from 'src/app/enum/datastate.enum';
import {CustomHttpResponse, Profile} from "../../interface/appstates";
import {NgForm} from "@angular/forms";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  profileState$: Observable<State<CustomHttpResponse<Profile>>>;
  private dataSubject = new BehaviorSubject<CustomHttpResponse<Profile>>(null);
  private isLoadingSubject = new BehaviorSubject<boolean>(false);
  isLoading$ = this.isLoadingSubject.asObservable();
  readonly DataState = DataState;

  constructor(private router: Router, private userService: UserService) {
  }

  ngOnInit(): void {
    this.profileState$ = this.userService.profile$()
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

  updateProfile(form: NgForm): void {
    this.isLoadingSubject.next(true);
    this.profileState$ = this.userService.update$(form.value)
      .pipe(
        map(response => {
          this.dataSubject.next({...response, data: response.data});
          this.isLoadingSubject.next(false);
          return {
            dataState: DataState.LOADED,
            appData: this.dataSubject.value
          };
        }),
        startWith({dataState: DataState.LOADED, appData: this.dataSubject.value}),
        catchError((error: string) => {
          this.isLoadingSubject.next(false);
          return of({dataState: DataState.LOADED, appData: this.dataSubject.value, error})
        })
      )
  }

  updatePassword(passwordUpdateForm: NgForm): void {
    this.isLoadingSubject.next(true);
    if (passwordUpdateForm.value.newPassword === passwordUpdateForm.value.confirmPassword) {
      this.performPasswordUpdate(passwordUpdateForm);
    } else {
      passwordUpdateForm.reset();
      console.log("Passwords don't match");
      this.isLoadingSubject.next(false);
    }
  }

  updateRole(roleForm: NgForm): void {
    this.isLoadingSubject.next(true);
    this.profileState$ = this.userService.updateUserRole$(roleForm.value.roleName)
      .pipe(
        map(response => {
          this.dataSubject.next({...response, data: response.data});
          this.isLoadingSubject.next(false);
          return {
            dataState: DataState.LOADED,
            appData: this.dataSubject.value
          };
        }),
        startWith({dataState: DataState.LOADED, appData: this.dataSubject.value}),
        catchError((error: string) => {
          this.isLoadingSubject.next(false);
          return of({dataState: DataState.LOADED, appData: this.dataSubject.value, error})
        })
        );
  }

  updateAccountSettings(settingsForm: NgForm): void {
    this.isLoadingSubject.next(true);
    this.profileState$ = this.userService.updateAccountSettings$(settingsForm.value)
      .pipe(
        map(response => {
          this.dataSubject.next({...response, data: response.data});
          this.isLoadingSubject.next(false);
          return {
            dataState: DataState.LOADED,
            appData: this.dataSubject.value
          };
        }),
        startWith({dataState: DataState.LOADED, appData: this.dataSubject.value}),
        catchError((error: string) => {
          this.isLoadingSubject.next(false);
          return of({dataState: DataState.LOADED, appData: this.dataSubject.value, error})
        })
      );
  }

  toggleMfa(): void {
    this.isLoadingSubject.next(true);
    this.profileState$ = this.userService.toggleMfa$()
      .pipe(
        map(response => {
          this.dataSubject.next({...response, data: response.data});
          this.isLoadingSubject.next(false);
          return {
            dataState: DataState.LOADED,
            appData: this.dataSubject.value
          };
        }),
        startWith({dataState: DataState.LOADED, appData: this.dataSubject.value}),
        catchError((error: string) => {
          this.isLoadingSubject.next(false);
          return of({dataState: DataState.LOADED, appData: this.dataSubject.value, error})
        })
      );
  }

  updateImage(image: File): void {
    this.isLoadingSubject.next(true);
    this.profileState$ = this.userService.updateImage$(this.getFormData(image))
      .pipe(
        map(response => {
          this.dataSubject.next({...response, data: response.data});
          this.isLoadingSubject.next(false);
          return {
            dataState: DataState.LOADED,
            appData: this.dataSubject.value
          };
        }),
        startWith({dataState: DataState.LOADED, appData: this.dataSubject.value}),
        catchError((error: string) => {
          this.isLoadingSubject.next(false);
          return of({dataState: DataState.LOADED, appData: this.dataSubject.value, error})
        })
      );
  }

  private performPasswordUpdate(passwordUpdateForm: NgForm): void {
    this.profileState$ = this.userService.updatePassword$(passwordUpdateForm.value)
      .pipe(
        map(response => {
          this.dataSubject.next({...response, data: response.data});
          this.isLoadingSubject.next(false);
          return {
            dataState: DataState.LOADED,
            appData: this.dataSubject.value
          };
        }),
        startWith({dataState: DataState.LOADED, appData: this.dataSubject.value}),
        catchError((error: string) => {
          this.isLoadingSubject.next(false);
          return of({dataState: DataState.LOADED, appData: this.dataSubject.value, error})
        })
      );
  }

  private getFormData(image: File): FormData {
    const formData = new FormData();
    formData.append('image', image);
    return formData
  }
}
