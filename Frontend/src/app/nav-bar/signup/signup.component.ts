import { Component } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { AuthService } from '../../shared/services/auth.service';
import { ToasterService } from '../../shared/services/toaster.service';

@Component({
  selector: 'signup',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule
  ],
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.scss'
})
export class SignupComponent {

  strongPasswordRegx: RegExp = /^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,25}$/;

  signupForm = new FormGroup({
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [Validators.required, Validators.minLength(8), Validators.maxLength(25), Validators.pattern(this.strongPasswordRegx)]),
    pin: new FormControl('', [Validators.minLength(6), Validators.maxLength(6)])
  });

  constructor(private dialog: MatDialog, private authService: AuthService, private toasterService: ToasterService) {}

  onSubmit(): void {
    if (this.signupForm.valid) {
      let email = this.signupForm.value.email;
      let password = this.signupForm.value.password;
      let pin = this.signupForm.value.pin;
      this.authService.register(this.signupForm.value).subscribe(result => {
        this.toasterService.createToaster(result.status, result.message);
      });
      this.dialog.closeAll(); // Close the dialog
    }
  }
}
