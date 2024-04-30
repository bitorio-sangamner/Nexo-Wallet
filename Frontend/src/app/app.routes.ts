import { Routes } from '@angular/router';
import { FutureComponent } from './display/future/future.component';
import { EarnComponent } from './display/earn/earn.component';
import { MarketComponent } from './display/market/market.component';
import { VerificationComponent } from './display/verification/verification.component';

export const routes: Routes = [
    {path:'future', component: FutureComponent},
    {path:'earn', component: EarnComponent},
    {path:'market', component: MarketComponent},
    {path:'verification', component: VerificationComponent}
];
