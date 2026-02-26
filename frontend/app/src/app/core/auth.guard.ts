import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import {AuthStore} from '../store/auth.store';


export const authGuard: CanActivateFn = (route, state) => {
  const store = inject(AuthStore);
  const router = inject(Router);


  // 1. On vérifie uniquement le signal isAuthenticated du Store.
  // Ce signal est mis à jour par l'appel /connected-user au démarrage.
  if (store.isAuthenticated()) {
    return true; // Accès autorisé
  } else {
    // 2. Si l'utilisateur n'est pas authentifié dans le Store,
    // on redirige vers la page de connexion.
    console.warn('Accès refusé !');
    // Accès refusé : redirection vers le login
    return router.parseUrl('/login');
  }
};
