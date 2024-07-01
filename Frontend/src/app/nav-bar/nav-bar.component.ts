import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { LoginComponent } from './login/login.component';
import { MatButtonModule } from '@angular/material/button';
import { SignupComponent } from './signup/signup.component';
import { MatIconModule } from '@angular/material/icon';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../shared/services/auth.service';
import { ToasterService } from '../shared/services/toaster.service';
import { CookieService } from 'ngx-cookie-service';

@Component({
  selector: 'nav-bar',
  standalone: true,
  imports: [
    MatButtonModule,
    MatIconModule,
    RouterModule
  ],
  providers: [CookieService],
  templateUrl: './nav-bar.component.html',
  styleUrl: './nav-bar.component.scss'
})
export class NavBarComponent{

  constructor(private dialog: MatDialog, private router: Router, private authService: AuthService, private toasterService: ToasterService, private cookieService: CookieService) {  }

  isLoggedIn(): boolean {
    return sessionStorage.getItem('loggedIn') === 'true';
  }

  openLoginDialog(): void {
    const dialogRef = this.dialog.open(LoginComponent, {
      width: '350px'
    });
  }

  openSignupDialog(): void {
    const dialogRef = this.dialog.open(SignupComponent, {
      width: '350px'
    });
  }

  logout(): void {
    console.log(JSON.stringify(this.cookieService.getAll()));
    this.authService.logoff('admin@yopmail.com').subscribe(result => {
      if (!result) {
        return;
      }
      if (result.status === 'success' && result.message.includes('logged out successfully')) {
        sessionStorage.setItem('loggedIn', 'false');
      }
      this.toasterService.createToaster(result.status, result.message);
    });
  }

  isBrowser(): boolean {
    return typeof window !== 'undefined' && typeof sessionStorage !== 'undefined';
  }
}
