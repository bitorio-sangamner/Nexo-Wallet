import { Routes } from '@angular/router';
import { FutureComponent } from './display/future/future.component';
import { EarnComponent } from './display/earn/earn.component';
import { MarketComponent } from './display/market/market.component';
import { VerificationComponent } from './display/verification/verification.component';
import { AppComponent } from './app.component';
import { VerifyEmailComponent } from './display/verify-email/verify-email.component';
import { ResetPasswordComponent } from './nav-bar/login/forgotpassword/resetpassword/resetpassword.component';
import { ForgotpasswordComponent } from './nav-bar/login/forgotpassword/forgotpassword.component';

export const routes: Routes = [
    {path:'future', component: FutureComponent},
    {path:'earn', component: EarnComponent},
    {path:'market', component: MarketComponent},
    {path:'verification', component: VerificationComponent},
    {path:'verify', component: VerifyEmailComponent},
    {path:'forgotpassword', component: ForgotpasswordComponent},
    {path:'resetpassword', loadComponent: () => import('./nav-bar/login/forgotpassword/resetpassword/resetpassword.component').then((x) => x.ResetPasswordComponent)}
];
