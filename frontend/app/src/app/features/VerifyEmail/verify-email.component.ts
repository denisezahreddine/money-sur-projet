import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthApi } from '../../api/auth.api';
import { AuthStore } from '../../store/auth.store';
import { User } from '../../api/models/user.models'

@Component({
  selector: 'app-verify-email',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="verify-container">
      <h2>Vérification de votre compte</h2>
      <p *ngIf="status === 'loading'">Validation en cours...</p>
      <p *ngIf="status === 'success'" style="color: green">Compte activé ! Redirection...</p>
      <p *ngIf="status === 'error'" style="color: red">{{ errorMessage }}</p>
    </div>
  `
})
export class VerifyEmailComponent implements OnInit {
  status: 'loading' | 'success' | 'error' = 'loading';
  errorMessage = '';

  private route = inject(ActivatedRoute);
  private api = inject(AuthApi);
  private router = inject(Router);
  private store = inject(AuthStore);

  ngOnInit() {
    const token = this.route.snapshot.queryParamMap.get('token');
    if (!token) {
      this.status = 'error';
      this.errorMessage = "Token manquant.";
      return;
    }

    this.api.verifyEmail(token).subscribe({
      next: (user: any) => {
        this.status = 'success';
        this.store.setLoginSuccess(user as User);
        setTimeout(() => this.router.navigate(['/home']), 2000);
      },
      error: (err) => {
        this.status = 'error';
        this.errorMessage = err.error?.message || "Le lien est invalide.";
      }
    });
  }
}
