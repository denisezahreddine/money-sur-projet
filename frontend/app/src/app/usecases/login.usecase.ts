import { inject, Injectable } from '@angular/core';
import { map, take, catchError } from 'rxjs/operators';
import { of,throwError } from 'rxjs';
import { AuthApi } from '../api/auth.api';
import { AuthStore } from '../store/auth.store';
import { CustomResponse } from '../entity/CustomResponse'
import { User } from '../api/models/user.models'


@Injectable({ providedIn: 'root' })
export class LoginUseCase {
  private api = inject(AuthApi);
  private store = inject(AuthStore);


  execute(credentials: any) {
    this.store.setLoading(true);
   // this.store.setError(null);

    return this.api.login(credentials).pipe(
      take(1),
      map((res: any) => {
        if (!res) throw new Error("Réponse vide du serveur");

        // On mappe vers notre interface User
        const user: User = {
          idUser: res.idUser,
          firstName: res.firstName,
          lastName: res.lastName,
          email: res.email,
          typeProfil: res.typeProfil,
          balance: res.balance ?? 0
        };

        this.store.setLoginSuccess(user);
        this.store.setLoading(false)
        return user;
        //return new CustomResponse(CustomResponse.success, res);
      }),
      catchError((err) => {
        const msg = err.error || "Identifiants invalides";
        //this.store.setError(msg);
        this.store.setLoading(false);
        return throwError(() => msg);
       // return of(new CustomResponse(CustomResponse.error, msg));
      })
    );
  }
}
