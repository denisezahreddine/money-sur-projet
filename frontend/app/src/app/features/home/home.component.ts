import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthStore } from '../../store/auth.store';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div style="text-align: center; margin-top: 50px; font-family: sans-serif;">
      @if (user()) {
        <h1>Bonjour {{ user()?.firstName }} {{ user()?.lastName }} !</h1>
        <p>Bienvenue sur votre espace MoneySur.</p>
        <p>Votre solde actuel : <strong>{{ user()?.balance }} €</strong></p>
      } @else {
        <h1>Chargement de votre profil...</h1>
      }
    </div>
  `
})
export class HomeComponent {
  private store = inject(AuthStore);

  // On branche le signal du store pour l'affichage réactif
  user = this.store.currentUser;
}
