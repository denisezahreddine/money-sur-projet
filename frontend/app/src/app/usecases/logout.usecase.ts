import { inject, Injectable } from '@angular/core';
import { AuthApi } from '../api/auth.api';
import { AuthStore } from '../store/auth.store';
import { Router } from '@angular/router';
import { finalize } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class LogoutUseCase {
  private authApi = inject(AuthApi);
  private authStore = inject(AuthStore);
  private router = inject(Router);

  execute() {
    this.authStore.setLoading(true);

    this.authApi.logout().pipe(
      finalize(() => this.authStore.setLoading(false))
    ).subscribe({
      next: () => {
        // On vide le Store
        this.authStore.setLogout();
        // 2. On redirige vers le Login
        this.router.navigate(['/login']);
      },
      error: (err) => {
        console.error('Logout failed', err);
        // Même si le serveur fait une erreur, on force souvent le logout local
        this.authStore.setLogout();
        this.router.navigate(['/login']);
      }
    });
  }
}
