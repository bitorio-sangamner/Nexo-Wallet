import { Component, signal } from '@angular/core';
import { MatDialog, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { AuthService } from '../../../shared/services/auth.service';
import { ToasterService } from '../../../shared/services/toaster.service';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-forgotpassword',
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
  templateUrl: './forgotpassword.component.html',
  styleUrl: './forgotpassword.component.scss'
})
export class ForgotpasswordComponent {
  constructor(private authService: AuthService, 
              private toasterService: ToasterService){}

  phide : boolean = true;
  rphide : boolean = true;
  readonly forgotPasswordForm = new FormGroup({
    email: new FormControl<string>('', [Validators.required, Validators.email])
  });

  
  forgotPassword() {
    const email: string = this.forgotPasswordForm.get('email')?.value ?? '';
    if (this.forgotPasswordForm.valid) {
      this.authService.forgotPassword(email);
    }
  }
}
