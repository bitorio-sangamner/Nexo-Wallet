import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { LoginComponent } from './login/login.component';
import { MatButtonModule } from '@angular/material/button';
import { SignupComponent } from './signup/signup.component';
import { MatIconModule } from '@angular/material/icon';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../shared/services/auth.service';
import { ToasterService } from '../shared/services/toaster.service';

@Component({
  selector: 'nav-bar',
  standalone: true,
  imports: [
    MatButtonModule,
    MatIconModule,
    RouterModule
  ],
  templateUrl: './nav-bar.component.html',
  styleUrl: './nav-bar.component.scss'
})
export class NavBarComponent {

  constructor(private dialog: MatDialog, private router: Router, private authService: AuthService, private toasterService: ToasterService) {}

  isLoggedIn: boolean = false;

  openLoginDialog(): void {
    const dialogRef = this.dialog.open(LoginComponent, {
      width: '350px'
    });

    dialogRef.afterClosed().subscribe(result => {
      this.isLoggedIn = result.data.status === 'success';
      if (result.data.message.includes("not verified") && result.data.status === "fail") {
        this.router.navigate(['verification', {email: result.email}]);
      }
      if (this.isLoggedIn)
        sessionStorage.setItem("loggedIn", "true");
    });
  }

  openSignupDialog(): void {
    const dialogRef = this.dialog.open(SignupComponent, {
      width: '350px'
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
    });
  }

  logout(): void {
    this.authService.logoff("admin@yopmail.com").subscribe(result => {
      if (result.status === "success" && result.message.includes("logged out successfully")) {
        sessionStorage.setItem("loggedIn", "false");
        this.isLoggedIn = false;
      }
      this.toasterService.createToaster(result.status, result.message);
    });
  }
}
