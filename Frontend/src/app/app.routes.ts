import { Routes } from '@angular/router';
import { FutureComponent } from './future/future.component';
import { EarnComponent } from './earn/earn.component';
import { MarketComponent } from './market/market.component';

export const routes: Routes = [
    {path:'future', component: FutureComponent},
    {path:'earn', component: EarnComponent},
    {path:'market', component: MarketComponent}
];
