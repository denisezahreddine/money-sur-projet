import {Routes} from '@angular/router';
import {authGuard} from './core/auth.guard';
import {LoginComponent} from './features/login/login.component';

export const routes: Routes = [
  /*{
    path: '',
    component: LayoutComponent,
    children: [
      {
        path: 'home',
        component: HomeComponent,
        canActivate: [authGuard]
      }],
  },*/

{
    path: '',
    component: LoginComponent
  }
];
