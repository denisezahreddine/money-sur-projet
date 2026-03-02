import {Routes} from '@angular/router';
import {authGuard} from './core/auth.guard';
import {LoginComponent} from './features/login/login.component';
import {RegisterComponent} from './features/register/register.component';
import {HomeComponent} from './features/home/home.component';
import {VerifyEmailComponent} from './features/VerifyEmail/verify-email.component';
import { guestGuard } from './core/guest.guard';

export const routes: Routes = [
  { path: 'login', component: LoginComponent,canActivate: [guestGuard] },//  Interdit si déjà connecté
  { path: 'register', component: RegisterComponent,canActivate: [guestGuard] },//Interdit si déjà connecté
  { path: 'verify-email', component: VerifyEmailComponent },
  { path: 'home', component: HomeComponent, canActivate: [authGuard] },// Protège la page si l'user n'est pas loggé
  // Redirection par défaut vers le login
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  // Capture toutes les mauvaises URLs et renvoie vers login
  { path: '**', redirectTo: 'login' }
];
