import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import {AuthStore} from '../store/auth.store';


export const guestGuard: CanActivateFn = (route, state) => {
  const store = inject(AuthStore);
  const router = inject(Router);

  if (!store.isAuthenticated()) {
    return true;
  } else {
    console.warn('Déjà connecté ! Redirection Home.');
    return router.parseUrl('/home');
  }
};
