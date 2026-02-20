
import { Injectable, inject } from '@angular/core';
import { Router } from '@angular/router';
import { take, tap } from 'rxjs';
import {AuthStore} from '../store/auth.store';
import {AuthApi} from '../api/auth.api';

@Injectable({ providedIn: 'root' })
export class LoginUseCase {
  private authApi = inject(AuthApi);
  private store = inject(AuthStore);
  private router = inject(Router);


  execute(clientCode: string, password: string): void {
    this.authApi.login(clientCode, password)
      .pipe(
        take(1)).subscribe({
        next: (response) => {
          // L'API a déjà fait le localStorage.setItem dans son 'tap'
          // Maintenant on prévient le STORE que c'est un succès
          this.store.setLoginSuccess(response.user.name,response.user.clientCode);
          //  Redirection vers la page des comptes
          this.router.navigate(['/home']);
        },
        error: (err) => {
          this.store.setError("Identifiants incorrects");
        }
      });
  }
}
