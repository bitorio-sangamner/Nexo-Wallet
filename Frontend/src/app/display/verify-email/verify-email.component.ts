import { Component, Input } from '@angular/core';
import { AuthService } from '../../shared/services/auth.service';
import { ToasterService } from '../../shared/services/toaster.service';

@Component({
  selector: 'app-verify-email',
  standalone: true,
  imports: [],
  templateUrl: './verify-email.component.html',
  styleUrl: './verify-email.component.scss'
})
export class VerifyEmailComponent {
  @Input('email') email!: string;
  @Input('token') token!: string;

  constructor(private authService: AuthService, private toasterService: ToasterService) {}

  verifyEmail() {
    this.authService.verifyemail(this.email, this.token).subscribe(result => {
      this.toasterService.createToaster(result.status, result.message);
      console.log(result);
    });
  }
}
