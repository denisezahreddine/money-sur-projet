import { inject, Injectable } from '@angular/core';
import { AuthApi } from '../api/auth.api';
import { User } from '../api/models/user.models';
import { Observable, throwError } from 'rxjs';
import { catchError, take } from 'rxjs/operators';
import { HttpErrorResponse } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class VerifyEmailUseCase {

  private api = inject(AuthApi);

  execute(token: string):Observable<User> {
    return this.api.verifyEmail(token).pipe(
      take(1),
      catchError((err: HttpErrorResponse) => {
        // Le backend renvoie { message: "..." }, on l'extrait
        const msg = err.error?.message || "Lien invalide ou expiré.";
        return throwError(() => new Error(msg));
      })
    );
  }
}
