import { HttpInterceptorFn, HttpRequest } from '@angular/common/http';
import { inject } from '@angular/core';

export const loggingInterceptor: HttpInterceptorFn = (req, next) => {
  console.log(req.url);
  return next(req);
};

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  let cookieService = inject(CookieService);
  const jwtoken = cookieService.get('X-AuthToken') ?? null;
  if (!jwtoken) {
    let newreq: HttpRequest<unknown> = req.clone({
      setHeaders: {
        Authorization: ('Bearer' + jwtoken)
      }
    })
  }
  return next(req);
};
