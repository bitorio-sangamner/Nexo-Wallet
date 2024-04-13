import { Component } from '@angular/core';
import { ReactiveFormsModule, FormGroup, Validators, FormControl } from '@angular/forms';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { AuthService } from '../../_services/auth.service';

@Component({
  selector: 'login',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatSelectModule,
    MatInputModule,
    MatButtonModule
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {

  loginForm = new FormGroup({
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [Validators.required, Validators.minLength(8), Validators.maxLength(25)])
  });

  constructor(private dialog: MatDialog, private authService: AuthService) {}

  onSubmit(): void {
    if (this.loginForm.valid) {
      // Here you can handle the login logic
      // For now, just close the dialog
      this.authService.login(this.loginForm.value).subscribe(result => {
        console.log(result);
      });
      console.log("frontend -> " + this.loginForm.value);
      this.dialog.closeAll(); // Close the dialog
    }
  }
}
