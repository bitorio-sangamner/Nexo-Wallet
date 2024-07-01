import { Component, Input } from '@angular/core';
import { AuthService } from '../../../shared/services/auth.service';
import { ToasterService } from '../../../shared/services/toaster.service';
import { Router } from '@angular/router';

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

  constructor(private authService: AuthService, private toasterService: ToasterService, private router: Router) {}

  verifyEmail() {
    this.authService.verifyemail(this.email, this.token).subscribe(result => {
      this.toasterService.createToaster(result.status, result.message);
      if (result.status === 'success')
        this.router.navigate(['dashboard']);
    });
  }
}
