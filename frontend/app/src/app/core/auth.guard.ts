import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import {AuthStore} from '../store/auth.store';


export const authGuard: CanActivateFn = (route, state) => {
  const store = inject(AuthStore);
  const router = inject(Router);
  const token = localStorage.getItem('access_token');

  // On vérifie le signal dans le store
  if (store.isAuthenticated()) {
    return true; // Accès autorisé
  } else {
    console.warn('Accès refusé !');
    // Accès refusé : redirection vers le login
    return router.parseUrl('/login');
  }
};
