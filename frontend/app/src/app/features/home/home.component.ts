import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthStore } from '../../store/auth.store';
import {ButtonComponent} from '../../shared/button.component/button.component';
import { LogoutUseCase } from '../../usecases/logout.usecase';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, ButtonComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'

})
export class HomeComponent {
  readonly store = inject(AuthStore);
  private readonly logoutUseCase = inject(LogoutUseCase);

  // On branche le signal du store pour l'affichage réactif
  user = this.store.currentUser;

  onLogout() {
    this.logoutUseCase.execute();
  }
}
