import { Component } from '@angular/core';
import { AuthService } from '../../../../shared/services/auth.service';
import { MatDialog, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { ToasterService } from '../../../../shared/services/toaster.service';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { CookieService } from 'ngx-cookie-service';

@Component({
  selector: 'app-resetpassword',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule
  ],
  templateUrl: './resetpassword.component.html',
  styleUrl: './resetpassword.component.scss'
})
export class ResetPasswordComponent {
  constructor(private authService: AuthService, 
    private dialog: MatDialog,
    private toasterService: ToasterService,
    private cookieService: CookieService) {
    }

  phide : boolean = true;
  rphide : boolean = true;
  ophide : boolean = true;
  strongPasswordRegx: RegExp = /^(?=[^A-Z]*[A-Z])(?=[^a-z]*[a-z])(?=\D*\d).{8,}$/;

  readonly forgotPasswordForm = new FormGroup({
    email: new FormControl<string>('', [Validators.required, Validators.email]),
    oldPassword: new FormControl<string>('', [Validators.required, Validators.minLength(8), Validators.maxLength(25), Validators.pattern(this.strongPasswordRegx)]),
    newPassword: new FormControl<string>('', [Validators.required, Validators.minLength(8), Validators.maxLength(25), Validators.pattern(this.strongPasswordRegx)]),
    retypePassword: new FormControl<string>('', [Validators.required, Validators.minLength(8), Validators.maxLength(25)])
  });


  resetPassword() {
    const email: string = this.forgotPasswordForm.get('email')?.value ?? '';
    const oldPassword: string = this.forgotPasswordForm.get('oldPassword')?.value ?? '';
    const newpassword: string = this.forgotPasswordForm.get('newPassword')?.value ?? '';
    const retypePassword: string = this.forgotPasswordForm.get('retypePassword')?.value ?? '';
    if (newpassword !== retypePassword) {
      this.toasterService.createToaster("fail", "Please type same password in retype password.")
      return;
    }
    if (this.forgotPasswordForm.valid && newpassword === retypePassword) {
      this.authService.resetPassword(email, oldPassword, newpassword);
    }
  }
}

