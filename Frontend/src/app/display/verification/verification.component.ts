import { Component, Input } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { AuthService } from '../../shared/services/auth.service';

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
  @Input() email!: string; 

  constructor(private authService: AuthService) { }
  verify() {
    this.authService.verify(this.email).subscribe(result => {
      console.log(result);
    });
  }
}
