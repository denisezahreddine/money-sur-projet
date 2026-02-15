//applique les regles de login ( 8 chiffre/6 chiffre) et appel auth.service
import {Injectable, inject} from '@angular/core';

import {Router} from '@angular/router';
import {AuthStore} from '../store/auth.store';
import {AuthApi} from '../api/auth.api';
import {take, tap} from 'rxjs';
import {CustomResponse} from '../entity/CustomResponse';


@Injectable({providedIn: 'root'})
export class RegisterUsecase {
  private api = inject(AuthApi);
  private store = inject(AuthStore);


  execute(clientCode: string, password: string) {
    return this.api.register(clientCode, password)
      .pipe(take(1),
        tap({
          next: (res) => {
            this.store.setLoginSuccess(res.user.name, res.user.clientCode);
            new CustomResponse(CustomResponse.success, {clientId: res.user.clientCode});
          },
          error: (err) => {
            this.store.setError(err.message);
            new CustomResponse(CustomResponse.error, {message: err.message});
          }
        }));
  }
}
