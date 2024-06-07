import { Injectable } from '@angular/core';
import { ToastrService } from 'ngx-toastr';

@Injectable({
  providedIn: 'root'
})
export class ToasterService {

  constructor(private toastrService: ToastrService) { }

  createToaster(status: string, message: string): void {
    switch (status) {
      case 'success':
        this.toastrService.success(message, 'Yes', {
          progressBar: true
        });
        break;
      case 'fail':
        this.toastrService.warning(message, 'No', {
          progressBar: true
        });
        break;
      case 'error':
        this.toastrService.error(message, 'Error', {
          progressBar: true
        });
        break;
    }
  }
}
