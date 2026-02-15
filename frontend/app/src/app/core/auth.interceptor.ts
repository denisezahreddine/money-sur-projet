import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import {AuthStore} from '../store/auth.store';


export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);
  const store = inject(AuthStore);
  const publicUrls = ['/register', '/login'];
  // Récupérer le token
  const token = localStorage.getItem('access_token');

  // Cloner la requête pour ajouter le Header si le token existe
  let authReq = req;
  if (token) {
    authReq = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
  }

  //  Envoyer la requête et surveiller les erreurs de retour
  return next(authReq).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401 && !publicUrls.some(url => req.url.includes(url))) {//500
        // Si le token est expiré ou invalide
        store.setLogout();
        router.navigate(['/login']);
      }
      return throwError(() => error);
    })
  );
};
