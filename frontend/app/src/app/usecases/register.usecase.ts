import { inject, Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { AuthApi } from '../api/auth.api';
import { AuthStore } from '../store/auth.store';
import { catchError, map, of, take,throwError } from 'rxjs';
import { User } from '../api/models/user.models';

@Injectable({ providedIn: 'root' })
export class RegisterUseCase {
  private api = inject(AuthApi);
  private store = inject(AuthStore);


  execute(userData: any) {
    this.store.setLoading(true);
    //this.store.setError(null);

    return this.api.register(userData).pipe(
      take(1),
      map((res: any) => {
        this.store.setLoading(false);
        return res;

      }),
      catchError((err) => {

        const msg = err.error || "Échec de l'inscription";
        this.store.setLoading(false);
        return throwError(() => msg);
      })
    );
  }
}
