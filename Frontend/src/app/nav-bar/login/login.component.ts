import { Component } from '@angular/core';
import { ReactiveFormsModule, FormGroup, Validators, FormControl } from '@angular/forms';
import { MatDialog, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';

import { AuthService } from '../../shared/services/auth.service';
import { APIResponse } from '../../shared/model/apiResponse';
import { ToasterService } from '../../shared/services/toaster.service';

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

  loginForm = new FormGroup({
    email: new FormControl<string>('', [Validators.required, Validators.email]),
    password: new FormControl<string>('', [Validators.required, Validators.minLength(8), Validators.maxLength(25)]),
    pin: new FormControl<number>(0, [Validators.minLength(6), Validators.maxLength(6)])
  });

  hide: boolean = true;

  constructor(private dialogRef: MatDialogRef<LoginComponent>, private authService: AuthService, private toasterService: ToasterService, private cookieService: CookieService) {}

  onSubmit(): void {
    let response: APIResponse;


    if (this.loginForm.valid) {
      const signUpCredentials: Object = {
        email: this.loginForm.get('email')?.value,
        password: this.loginForm.get('password')?.value,
        pin: this.loginForm.get('pin')?.value
      };
      
      this.authService.login(signUpCredentials).subscribe(result => {  
        this.toasterService.createToaster(result.status, result.message);
        console.log(this.cookieService.get('X-AuthToken'));
        this.dialogRef.close({data: result, email: this.loginForm.value.email}); // Close the dialog
      });
    }
  }
}