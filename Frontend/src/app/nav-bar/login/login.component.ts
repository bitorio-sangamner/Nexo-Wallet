import { Component, Signal, signal } from '@angular/core';
import { ReactiveFormsModule, FormGroup, Validators, FormControl } from '@angular/forms';
import { MatDialog, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

import { AuthService } from '../../shared/services/auth.service';
import { APIResponse } from '../../shared/model/apiResponse';
import { ToasterService } from '../../shared/services/toaster.service';
import { CookieService } from 'ngx-cookie-service';
import { ForgotpasswordComponent } from './forgotpassword/forgotpassword.component';
import { Router } from '@angular/router';
import { Sign } from 'crypto';
import { ErrorStateMatcher, ShowOnDirtyErrorStateMatcher } from '@angular/material/core';

@Component({
  selector: 'signin',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatSelectModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {

  constructor(private dialogRef: MatDialogRef<LoginComponent>, 
              private authService: AuthService, 
              private toasterService: ToasterService, 
              private dialog: MatDialog,
              private router: Router) {}
              
  strongPasswordRegx: RegExp = /^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,25}$/;

  readonly loginForm = new FormGroup({
    email: new FormControl<string>('', [Validators.required, Validators.email]),
    password: new FormControl<string>('', [Validators.required, Validators.minLength(6), Validators.maxLength(25), Validators.pattern(this.strongPasswordRegx)]),
    pin: new FormControl<number>(0, [Validators.minLength(6), Validators.maxLength(6)])
  });

  hide: boolean = true;
  buttonCLicked: string = 'no';

  onSubmit(): void {
    if (this.buttonCLicked === 'Login') {
      this.login()
    }
    if (this.buttonCLicked === 'ForgotPassword') {
      this.forgotPassword()
    }
    
  }

  login() {
    if (this.loginForm.valid) {
      const signUpCredentials: Object = {
        email: this.loginForm.get('email')?.value,
        password: this.loginForm.get('password')?.value,
        pin: this.loginForm.get('pin')?.value
      };
      
      this.authService.login(signUpCredentials).subscribe(result => {  
        this.toasterService.createToaster(result.status, result.message);
        console.log(result);
        if (!result.message) {
          return;
        }
        if (result.message.includes('not verified')) {
          this.router.navigate(['verification', {email: this.loginForm.get('email')?.value}]);
        }
        if (result.status === 'success' && result.message.includes('logged in successfully')){
          sessionStorage.setItem('loggedIn', 'true');
          this.router.navigate(['dashboard']);
        }
      });
    }
  }

  forgotPassword(): void {
    this.router.navigate(['forgotpassword']);
  }
}


