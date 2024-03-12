import {Component, Input} from '@angular/core';
import {Router} from "@angular/router";
import {UserService} from "../../service/user.service";
import {User} from "../../interface/user";

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {
  @Input() user: User;

  constructor(private router: Router, private userService: UserService) { }

  logout(): void {
    this.userService.logout();
    this.router.navigate(['/login']);
  }

}
