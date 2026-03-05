import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { AuthStore } from '../store/auth.store';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);
  const store = inject(AuthStore);

  const authReq = req.clone({ withCredentials: true });

  return next(authReq).pipe(
    catchError((error: HttpErrorResponse) => {
      // On ne redirige pas si on est déjà sur les pages de login/register
      const isPublicUrl = req.url.includes('/login') || req.url.includes('/register');

      if (error.status === 401 && !isPublicUrl) {
        store.setLogout();
        router.navigate(['/login']);
      }
      return throwError(() => error);
    })
  );
};
