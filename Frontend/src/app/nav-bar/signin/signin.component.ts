import { Component } from '@angular/core';
import { ReactiveFormsModule, FormGroup, Validators, FormControl } from '@angular/forms';
import { MatDialog, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';

import { AuthService } from '../../shared/model/services/auth.service';
import { APIResponse } from '../../shared/model/apiResponse';
import { ToastrModule, ToastrService } from 'ngx-toastr';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

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
  templateUrl: './signin.component.html',
  styleUrl: './signin.component.scss'
})
export class SigninComponent {

  loginForm = new FormGroup({
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [Validators.required, Validators.minLength(8), Validators.maxLength(25)])
  });

  hide: boolean = true;

  constructor(private dialogRef: MatDialogRef<SigninComponent>, private authService: AuthService, private toastrService: ToastrService) {}

  onSubmit(): void {
    let response: APIResponse;


    if (this.loginForm.valid) {
      
      // Here you can handle the signin logic
      // For now, just close the dialog
      this.authService.signin(this.loginForm.value).subscribe(result => {      
        const resultArray: string[] = JSON.stringify(result).split(',', 4);
        var apiResponse = Object.entries(result);
        response = new APIResponse(
          apiResponse[0][1],
          apiResponse[1][1],
          apiResponse[2][1]
        );
        if (response.status === "success") {
          this.toastrService.success(response.message, 'Yes', {
            progressBar: true
          });
        } else {
          this.toastrService.warning(response.message, 'No', {
            progressBar: true
          });
        }
        this.dialogRef.close({data: response, email: this.loginForm.value.email}); // Close the dialog
      });
    }
  }
}