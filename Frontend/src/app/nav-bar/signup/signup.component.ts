import { Component } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { AuthService } from '../../shared/model/services/auth.service';
import { HttpClientModule } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'signup',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    HttpClientModule
  ],
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.scss'
})
export class SignupComponent {

  signupForm = new FormGroup({
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [Validators.required, Validators.minLength(8), Validators.maxLength(25)]),
    pin: new FormControl('', [Validators.minLength(6), Validators.maxLength(6)])
  });

  constructor(private dialog: MatDialog, private authService: AuthService, private toastrService: ToastrService) {}

  onSubmit(): void {
    if (this.signupForm.valid) {
      // Here you can handle the signin logic
      // For now, just close the dialog
      let email = this.signupForm.value.email;
      let password = this.signupForm.value.password;
      let pin = this.signupForm.value.pin;
      this.authService.register(this.signupForm.value).subscribe(result => {
        console.log(result);
      });
      // console.log(this.authService.register(this.signupForm.value));
      this.dialog.closeAll(); // Close the dialog
    }
  }
}
