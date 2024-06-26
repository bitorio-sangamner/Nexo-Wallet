import { HttpEventType, HttpHeaders, HttpInterceptorFn, HttpRequest } from '@angular/common/http';
import { inject } from '@angular/core';
import { tap } from 'rxjs/internal/operators/tap';
import { ToasterService } from './shared/services/toaster.service';

export const loggingInterceptor: HttpInterceptorFn = (req, next) => {
  console.log(req.url);
  return next(req);
};

export const requestInterceptor: HttpInterceptorFn = (req, next) => {
  req = req.clone({
    setHeaders: {
      'Access-Control-Allow-Origin': '*'
    },
    withCredentials: true
  });
  return next(req);
};

export const responseErrorInterceptor: HttpInterceptorFn = (req, next) => {
  var toastr = inject(ToasterService);
  return next(req).pipe(
    tap(event => {
      if (event.type === HttpEventType.Response) {
        switch (event.status) {
          case 400:
          case 401:
          case 500: {
            console.error(req.url, ' responded with: ', event.statusText);
            toastr.createToaster('error', 'Server is busy right now. Please try again later.');
            break;
          }
        }
      }
    })
  );
}
