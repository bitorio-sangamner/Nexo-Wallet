import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { SigninComponent } from './signin/signin.component';
import { MatButtonModule } from '@angular/material/button';
import { SignupComponent } from './signup/signup.component';
import { MatIconModule } from '@angular/material/icon';
import { Router, RouterModule } from '@angular/router';

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
  constructor(private dialog: MatDialog, private router: Router) {}

  isLoggedIn: boolean = false;

  openLoginDialog(): void {
    const dialogRef = this.dialog.open(SigninComponent, {
      width: '350px'
    });

    dialogRef.afterClosed().subscribe(result => {
      this.isLoggedIn = result.data.status === 'success';
      console.log(result.data);
      if (result.data.message.includes("not verified") && result.data.status === "fail") {
        this.router.navigate(['verification', {email: result.email}]);
      }
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
}
