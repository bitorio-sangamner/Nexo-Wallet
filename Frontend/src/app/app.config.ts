import { ApplicationConfig } from '@angular/core';
import { provideRouter, withComponentInputBinding } from '@angular/router';

import { routes } from './app.routes';
import { provideClientHydration } from '@angular/platform-browser';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { provideHttpClient, withFetch, withInterceptors } from '@angular/common/http';
import { provideToastr } from 'ngx-toastr';
import { loggingInterceptor, requestInterceptor, responseErrorInterceptor } from './auth.interceptor';
import { ErrorStateMatcher, ShowOnDirtyErrorStateMatcher } from '@angular/material/core';

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes, withComponentInputBinding()), 
    provideClientHydration(), 
    provideAnimationsAsync(),
    provideHttpClient(
      withFetch(),
    withInterceptors([requestInterceptor, loggingInterceptor, responseErrorInterceptor])),
    provideToastr(), 
    provideAnimationsAsync(),
    {provide: ErrorStateMatcher, useClass: ShowOnDirtyErrorStateMatcher}
  ]
};
