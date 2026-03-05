import { ApplicationConfig, provideBrowserGlobalErrorListeners ,APP_INITIALIZER} from '@angular/core';
import { provideRouter } from '@angular/router';
import { routes } from './app.routes';
import { provideClientHydration, withEventReplay } from '@angular/platform-browser';
import {provideHttpClient, withInterceptors,withFetch} from '@angular/common/http';
import {authInterceptor} from './core/auth.interceptor';
import { AuthApi } from './api/auth.api';
import { AuthStore } from './store/auth.store';
import { catchError, of, tap } from 'rxjs';

//  la fonction qui va s'exécuter au démarrage
export function initializeApp(authApi: AuthApi, authStore: AuthStore) {
  return () => authApi.getCurrentUser().pipe(
    tap((user) => {
      // Si le cookie est valide, on restaure l'utilisateur dans le Store !
      authStore.setLoginSuccess(user);
      console.log("Utilisateur restauré avec succès :", user.email);
    }),
    catchError(() => {
      // Si le cookie est expiré ou absent, on s'assure que le Store est vide
      console.log("Aucune session active au démarrage.");
      // authStore.setLogout(); // Si tu as une méthode de déconnexion dans ton store
      return of(null); // On retourne null pour ne pas bloquer le démarrage de l'app
    })
  );
}
export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideRouter(routes),
    // Configuration pour autoriser les appels HTTP et activer ton intercepteur
    provideHttpClient(
      withFetch(),
      withInterceptors([authInterceptor])
    ),

    //On déclare notre initialiseur ici
    {
      provide: APP_INITIALIZER,
      useFactory: initializeApp,
      deps: [AuthApi, AuthStore], // On injecte l'API et le Store dans la fonction
      multi: true // multi: true est obligatoire pour APP_INITIALIZER
    }
  ]
};



