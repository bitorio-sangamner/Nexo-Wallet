import { Component, Input } from '@angular/core';
import { MatButton, MatButtonModule } from '@angular/material/button';
import { AuthService } from '../../shared/services/auth.service';
import { FormGroup } from '@angular/forms';
import { ToasterService } from '../../shared/services/toaster.service';

@Component({
  selector: 'app-verification',
  standalone: true,
  imports: [
    MatButtonModule
  ],
  templateUrl: './verification.component.html',
  styleUrl: './verification.component.scss'
})
export class VerificationComponent {
  @Input('email') email!: string;

  constructor(private authService: AuthService, private toasterService: ToasterService) {}

  verify() {
    this.authService.verify(this.email).subscribe(result => {
      this.toasterService.createToaster(result.status, result.message);
      console.log(result);
    });
  }
}
