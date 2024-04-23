import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { SigninComponent } from './signin/signin.component';
import { MatButtonModule } from '@angular/material/button';
import { SignupComponent } from './signup/signup.component';
import { MatIconModule } from '@angular/material/icon';
import { RouterModule } from '@angular/router';

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
  constructor(public dialog: MatDialog) {}

  isLoggedIn: boolean = false;

  openLoginDialog(): void {
    const dialogRef = this.dialog.open(SigninComponent, {
      width: '350px'
    });

    dialogRef.afterClosed().subscribe(result => {
      this.isLoggedIn = result.data.success;
      console.log(this.isLoggedIn,);
    });
  }

  openSignupDialog(): void {
    const dialogRef = this.dialog.open(SignupComponent, {
      width: '350px'
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log("Hello - " + result);
      console.log('The dialog was closed');
    });
  }
}
